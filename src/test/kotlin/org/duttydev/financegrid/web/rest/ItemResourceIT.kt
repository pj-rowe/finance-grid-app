package org.duttydev.financegrid.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.FinanceGridApp
import org.duttydev.financegrid.domain.Item
import org.duttydev.financegrid.domain.enumeration.ItemCategory
import org.duttydev.financegrid.domain.enumeration.ItemType
import org.duttydev.financegrid.repository.ItemRepository
import org.duttydev.financegrid.service.ItemService
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
 * Integration tests for the [ItemResource] REST controller.
 *
 * @see ItemResource
 */
@SpringBootTest(classes = [FinanceGridApp::class])
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemService: ItemService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restItemMockMvc: MockMvc

    private lateinit var item: Item

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val itemResource = ItemResource(itemService)
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        itemRepository.deleteAll()
        item = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createItem() {
        val databaseSizeBeforeCreate = itemRepository.findAll().size

        // Create the Item
        restItemMockMvc.perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(item))
        ).andExpect(status().isCreated)

        // Validate the Item in the database
        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1)
        val testItem = itemList[itemList.size - 1]
        assertThat(testItem.name).isEqualTo(DEFAULT_NAME)
        assertThat(testItem.itemType).isEqualTo(DEFAULT_ITEM_TYPE)
        assertThat(testItem.dueDate).isEqualTo(DEFAULT_DUE_DATE)
        assertThat(testItem.paid).isEqualTo(DEFAULT_PAID)
        assertThat(testItem.expectedAmount).isEqualTo(DEFAULT_EXPECTED_AMOUNT)
        assertThat(testItem.actualAmount).isEqualTo(DEFAULT_ACTUAL_AMOUNT)
        assertThat(testItem.category).isEqualTo(DEFAULT_CATEGORY)
    }

    @Test
    fun createItemWithExistingId() {
        val databaseSizeBeforeCreate = itemRepository.findAll().size

        // Create the Item with an existing ID
        item.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc.perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(item))
        ).andExpect(status().isBadRequest)

        // Validate the Item in the database
        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = itemRepository.findAll().size
        // set the field null
        item.name = null

        // Create the Item, which fails.

        restItemMockMvc.perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(item))
        ).andExpect(status().isBadRequest)

        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkItemTypeIsRequired() {
        val databaseSizeBeforeTest = itemRepository.findAll().size
        // set the field null
        item.itemType = null

        // Create the Item, which fails.

        restItemMockMvc.perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(item))
        ).andExpect(status().isBadRequest)

        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkExpectedAmountIsRequired() {
        val databaseSizeBeforeTest = itemRepository.findAll().size
        // set the field null
        item.expectedAmount = null

        // Create the Item, which fails.

        restItemMockMvc.perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(item))
        ).andExpect(status().isBadRequest)

        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkActualAmountIsRequired() {
        val databaseSizeBeforeTest = itemRepository.findAll().size
        // set the field null
        item.actualAmount = null

        // Create the Item, which fails.

        restItemMockMvc.perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(item))
        ).andExpect(status().isBadRequest)

        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllItems() {
        // Initialize the database
        itemRepository.save(item)

        // Get all the itemList
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].itemType").value(hasItem(DEFAULT_ITEM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID)))
            .andExpect(jsonPath("$.[*].expectedAmount").value(hasItem(DEFAULT_EXPECTED_AMOUNT?.toInt())))
            .andExpect(jsonPath("$.[*].actualAmount").value(hasItem(DEFAULT_ACTUAL_AMOUNT?.toInt())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
    }

    @Test
    @Throws(Exception::class)
    fun getItem() {
        // Initialize the database
        itemRepository.save(item)

        val id = item.id
        assertNotNull(id)

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.itemType").value(DEFAULT_ITEM_TYPE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID))
            .andExpect(jsonPath("$.expectedAmount").value(DEFAULT_EXPECTED_AMOUNT?.toInt()))
            .andExpect(jsonPath("$.actualAmount").value(DEFAULT_ACTUAL_AMOUNT?.toInt()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistingItem() {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    fun updateItem() {
        // Initialize the database
        itemService.save(item)

        val databaseSizeBeforeUpdate = itemRepository.findAll().size

        // Update the item
        val id = item.id
        assertNotNull(id)
        val updatedItem = itemRepository.findById(id).get()
        updatedItem.name = UPDATED_NAME
        updatedItem.itemType = UPDATED_ITEM_TYPE
        updatedItem.dueDate = UPDATED_DUE_DATE
        updatedItem.paid = UPDATED_PAID
        updatedItem.expectedAmount = UPDATED_EXPECTED_AMOUNT
        updatedItem.actualAmount = UPDATED_ACTUAL_AMOUNT
        updatedItem.category = UPDATED_CATEGORY

        restItemMockMvc.perform(
            put("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedItem))
        ).andExpect(status().isOk)

        // Validate the Item in the database
        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate)
        val testItem = itemList[itemList.size - 1]
        assertThat(testItem.name).isEqualTo(UPDATED_NAME)
        assertThat(testItem.itemType).isEqualTo(UPDATED_ITEM_TYPE)
        assertThat(testItem.dueDate).isEqualTo(UPDATED_DUE_DATE)
        assertThat(testItem.paid).isEqualTo(UPDATED_PAID)
        assertThat(testItem.expectedAmount).isEqualTo(UPDATED_EXPECTED_AMOUNT)
        assertThat(testItem.actualAmount).isEqualTo(UPDATED_ACTUAL_AMOUNT)
        assertThat(testItem.category).isEqualTo(UPDATED_CATEGORY)
    }

    @Test
    fun updateNonExistingItem() {
        val databaseSizeBeforeUpdate = itemRepository.findAll().size

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc.perform(
            put("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(item))
        ).andExpect(status().isBadRequest)

        // Validate the Item in the database
        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteItem() {
        // Initialize the database
        itemService.save(item)

        val databaseSizeBeforeDelete = itemRepository.findAll().size

        // Delete the item
        restItemMockMvc.perform(
            delete("/api/items/{id}", item.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val itemList = itemRepository.findAll()
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private val DEFAULT_ITEM_TYPE: ItemType = ItemType.ASSET
        private val UPDATED_ITEM_TYPE: ItemType = ItemType.EXPENSE

        private val DEFAULT_DUE_DATE: LocalDate = LocalDate.ofEpochDay(0L)
        private val UPDATED_DUE_DATE: LocalDate = LocalDate.now(ZoneId.systemDefault())

        private const val DEFAULT_PAID: Boolean = false
        private const val UPDATED_PAID: Boolean = true

        private val DEFAULT_EXPECTED_AMOUNT: BigDecimal = BigDecimal(1)
        private val UPDATED_EXPECTED_AMOUNT: BigDecimal = BigDecimal(2)

        private val DEFAULT_ACTUAL_AMOUNT: BigDecimal = BigDecimal(1)
        private val UPDATED_ACTUAL_AMOUNT: BigDecimal = BigDecimal(2)

        private val DEFAULT_CATEGORY: ItemCategory = ItemCategory.BILL
        private val UPDATED_CATEGORY: ItemCategory = ItemCategory.FUN

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Item {
            val item = Item(
                name = DEFAULT_NAME,
                itemType = DEFAULT_ITEM_TYPE,
                dueDate = DEFAULT_DUE_DATE,
                paid = DEFAULT_PAID,
                expectedAmount = DEFAULT_EXPECTED_AMOUNT,
                actualAmount = DEFAULT_ACTUAL_AMOUNT,
                category = DEFAULT_CATEGORY
            )

            return item
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Item {
            val item = Item(
                name = UPDATED_NAME,
                itemType = UPDATED_ITEM_TYPE,
                dueDate = UPDATED_DUE_DATE,
                paid = UPDATED_PAID,
                expectedAmount = UPDATED_EXPECTED_AMOUNT,
                actualAmount = UPDATED_ACTUAL_AMOUNT,
                category = UPDATED_CATEGORY
            )

            return item
        }
    }
}
