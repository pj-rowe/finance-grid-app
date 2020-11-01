package org.duttydev.financegrid.service
import org.duttydev.financegrid.domain.Budget
import org.duttydev.financegrid.repository.BudgetRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Service Implementation for managing [Budget].
 */
@Service
class BudgetService(
    private val budgetRepository: BudgetRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a budget.
     *
     * @param budget the entity to save.
     * @return the persisted entity.
     */
    fun save(budget: Budget): Budget {
        log.debug("Request to save Budget : $budget")
        return budgetRepository.save(budget)
    }

    /**
     * Get all the budgets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<Budget> {
        log.debug("Request to get all Budgets")
        return budgetRepository.findAll(pageable)
    }

    /**
     * Get one budget by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Budget> {
        log.debug("Request to get Budget : $id")
        return budgetRepository.findById(id)
    }

    /**
     * Delete the budget by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Budget : $id")

        budgetRepository.deleteById(id)
    }
}
