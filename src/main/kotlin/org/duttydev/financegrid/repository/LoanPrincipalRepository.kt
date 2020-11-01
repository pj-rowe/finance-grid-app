package org.duttydev.financegrid.repository

import org.duttydev.financegrid.domain.LoanPrincipal
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [LoanPrincipal] entity.
 */
@Suppress("unused")
@Repository
interface LoanPrincipalRepository : MongoRepository<LoanPrincipal, String>
