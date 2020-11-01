package org.duttydev.financegrid.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.FinanceGridApp
import org.duttydev.financegrid.domain.Loan
import org.duttydev.financegrid.repository.LoanRepository
import org.duttydev.financegrid.service.LoanService
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
import kotlin.test.assertNotNull

/**
 * Integration tests for the [LoanResource] REST controller.
 *
 * @see LoanResource
 */
@SpringBootTest(classes = [FinanceGridApp::class])
@AutoConfigureMockMvc
@WithMockUser
class LoanResourceIT {

    @Autowired
    private lateinit var loanRepository: LoanRepository

    @Autowired
    private lateinit var loanService: LoanService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restLoanMockMvc: MockMvc

    private lateinit var loan: Loan

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val loanResource = LoanResource(loanService)
        this.restLoanMockMvc = MockMvcBuilders.standaloneSetup(loanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        loanRepository.deleteAll()
        loan = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createLoan() {
        val databaseSizeBeforeCreate = loanRepository.findAll().size

        // Create the Loan
        restLoanMockMvc.perform(
            post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loan))
        ).andExpect(status().isCreated)

        // Validate the Loan in the database
        val loanList = loanRepository.findAll()
        assertThat(loanList).hasSize(databaseSizeBeforeCreate + 1)
        val testLoan = loanList[loanList.size - 1]
        assertThat(testLoan.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createLoanWithExistingId() {
        val databaseSizeBeforeCreate = loanRepository.findAll().size

        // Create the Loan with an existing ID
        loan.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanMockMvc.perform(
            post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loan))
        ).andExpect(status().isBadRequest)

        // Validate the Loan in the database
        val loanList = loanRepository.findAll()
        assertThat(loanList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = loanRepository.findAll().size
        // set the field null
        loan.name = null

        // Create the Loan, which fails.

        restLoanMockMvc.perform(
            post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loan))
        ).andExpect(status().isBadRequest)

        val loanList = loanRepository.findAll()
        assertThat(loanList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllLoans() {
        // Initialize the database
        loanRepository.save(loan)

        // Get all the loanList
        restLoanMockMvc.perform(get("/api/loans?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loan.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }

    @Test
    @Throws(Exception::class)
    fun getLoan() {
        // Initialize the database
        loanRepository.save(loan)

        val id = loan.id
        assertNotNull(id)

        // Get the loan
        restLoanMockMvc.perform(get("/api/loans/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loan.id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistingLoan() {
        // Get the loan
        restLoanMockMvc.perform(get("/api/loans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    fun updateLoan() {
        // Initialize the database
        loanService.save(loan)

        val databaseSizeBeforeUpdate = loanRepository.findAll().size

        // Update the loan
        val id = loan.id
        assertNotNull(id)
        val updatedLoan = loanRepository.findById(id).get()
        updatedLoan.name = UPDATED_NAME

        restLoanMockMvc.perform(
            put("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedLoan))
        ).andExpect(status().isOk)

        // Validate the Loan in the database
        val loanList = loanRepository.findAll()
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate)
        val testLoan = loanList[loanList.size - 1]
        assertThat(testLoan.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingLoan() {
        val databaseSizeBeforeUpdate = loanRepository.findAll().size

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanMockMvc.perform(
            put("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(loan))
        ).andExpect(status().isBadRequest)

        // Validate the Loan in the database
        val loanList = loanRepository.findAll()
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteLoan() {
        // Initialize the database
        loanService.save(loan)

        val databaseSizeBeforeDelete = loanRepository.findAll().size

        // Delete the loan
        restLoanMockMvc.perform(
            delete("/api/loans/{id}", loan.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val loanList = loanRepository.findAll()
        assertThat(loanList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Loan {
            val loan = Loan(
                name = DEFAULT_NAME
            )

            return loan
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Loan {
            val loan = Loan(
                name = UPDATED_NAME
            )

            return loan
        }
    }
}
