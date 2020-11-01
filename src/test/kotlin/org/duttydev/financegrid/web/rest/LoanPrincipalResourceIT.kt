package org.duttydev.financegrid.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.FinanceGridApp
import org.duttydev.financegrid.domain.Loan
import org.duttydev.financegrid.domain.LoanPrincipal
import org.duttydev.financegrid.repository.LoanPrincipalRepository
import org.duttydev.financegrid.service.LoanPrincipalService
import org.duttydev.financegrid.web.rest.errors.ExceptionTranslator
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertNotNull

/**
 * Integration tests for the [LoanPrincipalResource] REST controller.
 *
 * @see LoanPrincipalResource
 */
@SpringBootTest(classes = [FinanceGridApp::class])
@AutoConfigureMockMvc
@WithMockUser
class LoanPrincipalResourceIT {

    @Autowired
    private lateinit var loanPrincipalRepository: LoanPrincipalRepository

    @Autowired
    private lateinit var loanPrincipalService: LoanPrincipalService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restLoanPrincipalMockMvc: MockMvc

    private lateinit var loanPrincipal: LoanPrincipal

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val loanPrincipalResource = LoanPrincipalResource(loanPrincipalService)
        this.restLoanPrincipalMockMvc = MockMvcBuilders.standaloneSetup(loanPrincipalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        loanPrincipalRepository.deleteAll()
        loanPrincipal = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createLoanPrincipal() {
        val databaseSizeBeforeCreate = loanPrincipalRepository.findAll().size

        // Create the LoanPrincipal
        restLoanPrincipalMockMvc.perform(
            post("/api/loan-principals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loanPrincipal))
        ).andExpect(status().isCreated)

        // Validate the LoanPrincipal in the database
        val loanPrincipalList = loanPrincipalRepository.findAll()
        assertThat(loanPrincipalList).hasSize(databaseSizeBeforeCreate + 1)
        val testLoanPrincipal = loanPrincipalList[loanPrincipalList.size - 1]
        assertThat(testLoanPrincipal.date).isEqualTo(DEFAULT_DATE)
        assertThat(testLoanPrincipal.amount).isEqualTo(DEFAULT_AMOUNT)
    }

    @Test
    fun createLoanPrincipalWithExistingId() {
        val databaseSizeBeforeCreate = loanPrincipalRepository.findAll().size

        // Create the LoanPrincipal with an existing ID
        loanPrincipal.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanPrincipalMockMvc.perform(
            post("/api/loan-principals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loanPrincipal))
        ).andExpect(status().isBadRequest)

        // Validate the LoanPrincipal in the database
        val loanPrincipalList = loanPrincipalRepository.findAll()
        assertThat(loanPrincipalList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkDateIsRequired() {
        val databaseSizeBeforeTest = loanPrincipalRepository.findAll().size
        // set the field null
        loanPrincipal.date = null

        // Create the LoanPrincipal, which fails.

        restLoanPrincipalMockMvc.perform(
            post("/api/loan-principals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loanPrincipal))
        ).andExpect(status().isBadRequest)

        val loanPrincipalList = loanPrincipalRepository.findAll()
        assertThat(loanPrincipalList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkAmountIsRequired() {
        val databaseSizeBeforeTest = loanPrincipalRepository.findAll().size
        // set the field null
        loanPrincipal.amount = null

        // Create the LoanPrincipal, which fails.

        restLoanPrincipalMockMvc.perform(
            post("/api/loan-principals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loanPrincipal))
        ).andExpect(status().isBadRequest)

        val loanPrincipalList = loanPrincipalRepository.findAll()
        assertThat(loanPrincipalList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllLoanPrincipals() {
        // Initialize the database
        loanPrincipalRepository.save(loanPrincipal)

        // Get all the loanPrincipalList
        restLoanPrincipalMockMvc.perform(get("/api/loan-principals?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanPrincipal.id)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT?.toInt())))
    }

    @Test
    @Throws(Exception::class)
    fun getLoanPrincipal() {
        // Initialize the database
        loanPrincipalRepository.save(loanPrincipal)

        val id = loanPrincipal.id
        assertNotNull(id)

        // Get the loanPrincipal
        restLoanPrincipalMockMvc.perform(get("/api/loan-principals/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanPrincipal.id))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT?.toInt()))
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistingLoanPrincipal() {
        // Get the loanPrincipal
        restLoanPrincipalMockMvc.perform(get("/api/loan-principals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    fun updateLoanPrincipal() {
        // Initialize the database
        loanPrincipalService.save(loanPrincipal)

        val databaseSizeBeforeUpdate = loanPrincipalRepository.findAll().size

        // Update the loanPrincipal
        val id = loanPrincipal.id
        assertNotNull(id)
        val updatedLoanPrincipal = loanPrincipalRepository.findById(id).get()
        updatedLoanPrincipal.date = UPDATED_DATE
        updatedLoanPrincipal.amount = UPDATED_AMOUNT

        restLoanPrincipalMockMvc.perform(
            put("/api/loan-principals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedLoanPrincipal))
        ).andExpect(status().isOk)

        // Validate the LoanPrincipal in the database
        val loanPrincipalList = loanPrincipalRepository.findAll()
        assertThat(loanPrincipalList).hasSize(databaseSizeBeforeUpdate)
        val testLoanPrincipal = loanPrincipalList[loanPrincipalList.size - 1]
        assertThat(testLoanPrincipal.date).isEqualTo(UPDATED_DATE)
        assertThat(testLoanPrincipal.amount).isEqualTo(UPDATED_AMOUNT)
    }

    @Test
    fun updateNonExistingLoanPrincipal() {
        val databaseSizeBeforeUpdate = loanPrincipalRepository.findAll().size

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanPrincipalMockMvc.perform(
            put("/api/loan-principals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loanPrincipal))
        ).andExpect(status().isBadRequest)

        // Validate the LoanPrincipal in the database
        val loanPrincipalList = loanPrincipalRepository.findAll()
        assertThat(loanPrincipalList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteLoanPrincipal() {
        // Initialize the database
        loanPrincipalService.save(loanPrincipal)

        val databaseSizeBeforeDelete = loanPrincipalRepository.findAll().size

        // Delete the loanPrincipal
        restLoanPrincipalMockMvc.perform(
            delete("/api/loan-principals/{id}", loanPrincipal.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val loanPrincipalList = loanPrincipalRepository.findAll()
        assertThat(loanPrincipalList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val DEFAULT_DATE: LocalDate = LocalDate.ofEpochDay(0L)
        private val UPDATED_DATE: LocalDate = LocalDate.now(ZoneId.systemDefault())

        private val DEFAULT_AMOUNT: BigDecimal = BigDecimal(1)
        private val UPDATED_AMOUNT: BigDecimal = BigDecimal(2)

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): LoanPrincipal {
            val loanPrincipal = LoanPrincipal(
                date = DEFAULT_DATE,
                amount = DEFAULT_AMOUNT
            )

            // Add required entity
            val loan: Loan
            loan = LoanResourceIT.createEntity()
            loan.id = "fixed-id-for-tests"
            loanPrincipal.loan = loan
            return loanPrincipal
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): LoanPrincipal {
            val loanPrincipal = LoanPrincipal(
                date = UPDATED_DATE,
                amount = UPDATED_AMOUNT
            )

            // Add required entity
            val loan: Loan
            loan = LoanResourceIT.createUpdatedEntity()
            loan.id = "fixed-id-for-tests"
            loanPrincipal.loan = loan
            return loanPrincipal
        }
    }
}
