package org.duttydev.financegrid.service
import org.duttydev.financegrid.domain.LoanPrincipal
import org.duttydev.financegrid.repository.LoanPrincipalRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Service Implementation for managing [LoanPrincipal].
 */
@Service
class LoanPrincipalService(
    private val loanPrincipalRepository: LoanPrincipalRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a loanPrincipal.
     *
     * @param loanPrincipal the entity to save.
     * @return the persisted entity.
     */
    fun save(loanPrincipal: LoanPrincipal): LoanPrincipal {
        log.debug("Request to save LoanPrincipal : $loanPrincipal")
        return loanPrincipalRepository.save(loanPrincipal)
    }

    /**
     * Get all the loanPrincipals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<LoanPrincipal> {
        log.debug("Request to get all LoanPrincipals")
        return loanPrincipalRepository.findAll(pageable)
    }

    /**
     * Get one loanPrincipal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<LoanPrincipal> {
        log.debug("Request to get LoanPrincipal : $id")
        return loanPrincipalRepository.findById(id)
    }

    /**
     * Delete the loanPrincipal by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete LoanPrincipal : $id")

        loanPrincipalRepository.deleteById(id)
    }
}
