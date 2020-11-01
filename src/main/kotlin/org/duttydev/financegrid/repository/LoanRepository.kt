package org.duttydev.financegrid.repository

import org.duttydev.financegrid.domain.Loan
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Loan] entity.
 */
@Suppress("unused")
@Repository
interface LoanRepository : MongoRepository<Loan, String>
