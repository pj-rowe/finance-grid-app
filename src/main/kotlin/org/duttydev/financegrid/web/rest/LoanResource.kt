package org.duttydev.financegrid.web.rest

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.duttydev.financegrid.domain.Loan
import org.duttydev.financegrid.service.LoanService
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

private const val ENTITY_NAME = "loan"
/**
 * REST controller for managing [org.duttydev.financegrid.domain.Loan].
 */
@RestController
@RequestMapping("/api")
class LoanResource(
    private val loanService: LoanService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /loans` : Create a new loan.
     *
     * @param loan the loan to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new loan, or with status `400 (Bad Request)` if the loan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loans")
    fun createLoan(@Valid @RequestBody loan: Loan): ResponseEntity<Loan> {
        log.debug("REST request to save Loan : $loan")
        if (loan.id != null) {
            throw BadRequestAlertException(
                "A new loan cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            )
        }
        val result = loanService.save(loan)
        return ResponseEntity.created(URI("/api/loans/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * `PUT  /loans` : Updates an existing loan.
     *
     * @param loan the loan to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated loan,
     * or with status `400 (Bad Request)` if the loan is not valid,
     * or with status `500 (Internal Server Error)` if the loan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loans")
    fun updateLoan(@Valid @RequestBody loan: Loan): ResponseEntity<Loan> {
        log.debug("REST request to update Loan : $loan")
        if (loan.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = loanService.save(loan)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    loan.id
                )
            )
            .body(result)
    }
    /**
     * `GET  /loans` : get all the loans.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of loans in body.
     */
    @GetMapping("/loans")
    fun getAllLoans(pageable: Pageable): ResponseEntity<List<Loan>> {
        log.debug("REST request to get a page of Loans")
        val page = loanService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /loans/:id` : get the "id" loan.
     *
     * @param id the id of the loan to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the loan, or with status `404 (Not Found)`.
     */
    @GetMapping("/loans/{id}")
    fun getLoan(@PathVariable id: String): ResponseEntity<Loan> {
        log.debug("REST request to get Loan : $id")
        val loan = loanService.findOne(id)
        return ResponseUtil.wrapOrNotFound(loan)
    }
    /**
     *  `DELETE  /loans/:id` : delete the "id" loan.
     *
     * @param id the id of the loan to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/loans/{id}")
    fun deleteLoan(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Loan : $id")

        loanService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
