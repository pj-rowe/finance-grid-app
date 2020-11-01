package org.duttydev.financegrid.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.FinanceGridApp
import org.duttydev.financegrid.domain.Budget
import org.duttydev.financegrid.repository.BudgetRepository
import org.duttydev.financegrid.service.BudgetService
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
 * Integration tests for the [BudgetResource] REST controller.
 *
 * @see BudgetResource
 */
@SpringBootTest(classes = [FinanceGridApp::class])
@AutoConfigureMockMvc
@WithMockUser
class BudgetResourceIT {

    @Autowired
    private lateinit var budgetRepository: BudgetRepository

    @Autowired
    private lateinit var budgetService: BudgetService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restBudgetMockMvc: MockMvc

    private lateinit var budget: Budget

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val budgetResource = BudgetResource(budgetService)
        this.restBudgetMockMvc = MockMvcBuilders.standaloneSetup(budgetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        budgetRepository.deleteAll()
        budget = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createBudget() {
        val databaseSizeBeforeCreate = budgetRepository.findAll().size

        // Create the Budget
        restBudgetMockMvc.perform(
            post("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(budget))
        ).andExpect(status().isCreated)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate + 1)
        val testBudget = budgetList[budgetList.size - 1]
        assertThat(testBudget.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createBudgetWithExistingId() {
        val databaseSizeBeforeCreate = budgetRepository.findAll().size

        // Create the Budget with an existing ID
        budget.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetMockMvc.perform(
            post("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(budget))
        ).andExpect(status().isBadRequest)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = budgetRepository.findAll().size
        // set the field null
        budget.name = null

        // Create the Budget, which fails.

        restBudgetMockMvc.perform(
            post("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(budget))
        ).andExpect(status().isBadRequest)

        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllBudgets() {
        // Initialize the database
        budgetRepository.save(budget)

        // Get all the budgetList
        restBudgetMockMvc.perform(get("/api/budgets?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }

    @Test
    @Throws(Exception::class)
    fun getBudget() {
        // Initialize the database
        budgetRepository.save(budget)

        val id = budget.id
        assertNotNull(id)

        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budget.id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistingBudget() {
        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    fun updateBudget() {
        // Initialize the database
        budgetService.save(budget)

        val databaseSizeBeforeUpdate = budgetRepository.findAll().size

        // Update the budget
        val id = budget.id
        assertNotNull(id)
        val updatedBudget = budgetRepository.findById(id).get()
        updatedBudget.name = UPDATED_NAME

        restBudgetMockMvc.perform(
            put("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedBudget))
        ).andExpect(status().isOk)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate)
        val testBudget = budgetList[budgetList.size - 1]
        assertThat(testBudget.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingBudget() {
        val databaseSizeBeforeUpdate = budgetRepository.findAll().size

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetMockMvc.perform(
            put("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(budget))
        ).andExpect(status().isBadRequest)

        // Validate the Budget in the database
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteBudget() {
        // Initialize the database
        budgetService.save(budget)

        val databaseSizeBeforeDelete = budgetRepository.findAll().size

        // Delete the budget
        restBudgetMockMvc.perform(
            delete("/api/budgets/{id}", budget.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val budgetList = budgetRepository.findAll()
        assertThat(budgetList).hasSize(databaseSizeBeforeDelete - 1)
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
        fun createEntity(): Budget {
            val budget = Budget(
                name = DEFAULT_NAME
            )

            return budget
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Budget {
            val budget = Budget(
                name = UPDATED_NAME
            )

            return budget
        }
    }
}
