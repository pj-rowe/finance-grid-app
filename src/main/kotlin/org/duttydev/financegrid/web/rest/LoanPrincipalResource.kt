package org.duttydev.financegrid.web.rest

import org.duttydev.financegrid.service.LoanPrincipalService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

private const val ENTITY_NAME = "loanPrincipal"
/**
 * REST controller for managing [org.duttydev.financegrid.domain.LoanPrincipal].
 */
@RestController
@RequestMapping("/api")
class LoanPrincipalResource(
    private val loanPrincipalService: LoanPrincipalService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

//    /**
//     * `POST  /loan-principals` : Create a new loanPrincipal.
//     *
//     * @param loanPrincipal the loanPrincipal to create.
//     * @return the [ResponseEntity] with status `201 (Created)` and with body the new loanPrincipal, or with status `400 (Bad Request)` if the loanPrincipal has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("/loan-principals")
//    fun createLoanPrincipal(@Valid @RequestBody loanPrincipal: LoanPrincipal): ResponseEntity<LoanPrincipal> {
//        log.debug("REST request to save LoanPrincipal : $loanPrincipal")
//        if (loanPrincipal.id != null) {
//            throw BadRequestAlertException(
//                "A new loanPrincipal cannot already have an ID",
//                ENTITY_NAME,
//                "idexists"
//            )
//        }
//        val result = loanPrincipalService.save(loanPrincipal)
//        return ResponseEntity.created(URI("/api/loan-principals/${result.id}"))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
//            .body(result)
//    }
//
//    /**
//     * `PUT  /loan-principals` : Updates an existing loanPrincipal.
//     *
//     * @param loanPrincipal the loanPrincipal to update.
//     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated loanPrincipal,
//     * or with status `400 (Bad Request)` if the loanPrincipal is not valid,
//     * or with status `500 (Internal Server Error)` if the loanPrincipal couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/loan-principals")
//    fun updateLoanPrincipal(@Valid @RequestBody loanPrincipal: LoanPrincipal): ResponseEntity<LoanPrincipal> {
//        log.debug("REST request to update LoanPrincipal : $loanPrincipal")
//        if (loanPrincipal.id == null) {
//            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
//        }
//        val result = loanPrincipalService.save(loanPrincipal)
//        return ResponseEntity.ok()
//            .headers(
//                HeaderUtil.createEntityUpdateAlert(
//                    applicationName,
//                    true,
//                    ENTITY_NAME,
//                    loanPrincipal.id
//                )
//            )
//            .body(result)
//    }
//    /**
//     * `GET  /loan-principals` : get all the loanPrincipals.
//     *
//     * @param pageable the pagination information.
//
//     * @return the [ResponseEntity] with status `200 (OK)` and the list of loanPrincipals in body.
//     */
//    @GetMapping("/loan-principals")
//    fun getAllLoanPrincipals(pageable: Pageable): ResponseEntity<List<LoanPrincipal>> {
//        log.debug("REST request to get a page of LoanPrincipals")
//        val page = loanPrincipalService.findAll(pageable)
//        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
//        return ResponseEntity.ok().headers(headers).body(page.content)
//    }
//
//    /**
//     * `GET  /loan-principals/:id` : get the "id" loanPrincipal.
//     *
//     * @param id the id of the loanPrincipal to retrieve.
//     * @return the [ResponseEntity] with status `200 (OK)` and with body the loanPrincipal, or with status `404 (Not Found)`.
//     */
//    @GetMapping("/loan-principals/{id}")
//    fun getLoanPrincipal(@PathVariable id: String): ResponseEntity<LoanPrincipal> {
//        log.debug("REST request to get LoanPrincipal : $id")
//        val loanPrincipal = loanPrincipalService.findOne(id)
//        return ResponseUtil.wrapOrNotFound(loanPrincipal)
//    }
//    /**
//     *  `DELETE  /loan-principals/:id` : delete the "id" loanPrincipal.
//     *
//     * @param id the id of the loanPrincipal to delete.
//     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
//     */
//    @DeleteMapping("/loan-principals/{id}")
//    fun deleteLoanPrincipal(@PathVariable id: String): ResponseEntity<Void> {
//        log.debug("REST request to delete LoanPrincipal : $id")
//
//        loanPrincipalService.delete(id)
//        return ResponseEntity.noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
//    }
}
