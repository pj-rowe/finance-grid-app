package org.duttydev.financegrid.web.rest

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.duttydev.financegrid.domain.Budget
import org.duttydev.financegrid.service.BudgetService
import org.duttydev.financegrid.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.net.URISyntaxException
import javax.validation.Valid

private const val ENTITY_NAME = "budget"
/**
 * REST controller for managing [org.duttydev.financegrid.domain.Budget].
 */
@RestController
@RequestMapping("/api")
class BudgetResource(
    private val budgetService: BudgetService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /budgets` : Create a new budget.
     *
     * @param budget the budget to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new budget, or with status `400 (Bad Request)` if the budget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/budgets")
    fun createBudget(@Valid @RequestBody budget: Budget): ResponseEntity<Budget> {
        log.debug("REST request to save Budget : $budget")
        if (budget.id != null) {
            throw BadRequestAlertException(
                "A new budget cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            )
        }
        val result = budgetService.save(budget)
        return ResponseEntity.created(URI("/api/budgets/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * `PUT  /budgets` : Updates an existing budget.
     *
     * @param budget the budget to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated budget,
     * or with status `400 (Bad Request)` if the budget is not valid,
     * or with status `500 (Internal Server Error)` if the budget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/budgets")
    fun updateBudget(@Valid @RequestBody budget: Budget): ResponseEntity<Budget> {
        log.debug("REST request to update Budget : $budget")
        if (budget.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = budgetService.save(budget)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    budget.id
                )
            )
            .body(result)
    }
    /**
     * `GET  /budgets` : get all the budgets.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of budgets in body.
     */
    @GetMapping("/budgets")
    fun getAllBudgets(pageable: Pageable): ResponseEntity<List<Budget>> {
        log.debug("REST request to get a page of Budgets")
        val page = budgetService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /budgets/:id` : get the "id" budget.
     *
     * @param id the id of the budget to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the budget, or with status `404 (Not Found)`.
     */
    @GetMapping("/budgets/{id}")
    fun getBudget(@PathVariable id: String): ResponseEntity<Budget> {
        log.debug("REST request to get Budget : $id")
        val budget = budgetService.findOne(id)
        return ResponseUtil.wrapOrNotFound(budget)
    }
    /**
     *  `DELETE  /budgets/:id` : delete the "id" budget.
     *
     * @param id the id of the budget to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/budgets/{id}")
    fun deleteBudget(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Budget : $id")

        budgetService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
