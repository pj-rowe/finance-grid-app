package org.duttydev.financegrid.repository

import org.duttydev.financegrid.domain.Budget
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Budget] entity.
 */
@Suppress("unused")
@Repository
interface BudgetRepository : MongoRepository<Budget, String>
