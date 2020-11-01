package org.duttydev.financegrid.service
import org.duttydev.financegrid.domain.Loan
import org.duttydev.financegrid.repository.LoanRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Service Implementation for managing [Loan].
 */
@Service
class LoanService(
    private val loanRepository: LoanRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a loan.
     *
     * @param loan the entity to save.
     * @return the persisted entity.
     */
    fun save(loan: Loan): Loan {
        log.debug("Request to save Loan : $loan")
        return loanRepository.save(loan)
    }

    /**
     * Get all the loans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<Loan> {
        log.debug("Request to get all Loans")
        return loanRepository.findAll(pageable)
    }

    /**
     * Get one loan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Loan> {
        log.debug("Request to get Loan : $id")
        return loanRepository.findById(id)
    }

    /**
     * Delete the loan by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Loan : $id")

        loanRepository.deleteById(id)
    }
}
