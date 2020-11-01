package org.duttydev.financegrid.web.rest

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.duttydev.financegrid.domain.Item
import org.duttydev.financegrid.service.ItemService
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

private const val ENTITY_NAME = "item"
/**
 * REST controller for managing [org.duttydev.financegrid.domain.Item].
 */
@RestController
@RequestMapping("/api")
class ItemResource(
    private val itemService: ItemService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /items` : Create a new item.
     *
     * @param item the item to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new item, or with status `400 (Bad Request)` if the item has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/items")
    fun createItem(@Valid @RequestBody item: Item): ResponseEntity<Item> {
        log.debug("REST request to save Item : $item")
        if (item.id != null) {
            throw BadRequestAlertException(
                "A new item cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            )
        }
        val result = itemService.save(item)
        return ResponseEntity.created(URI("/api/items/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * `PUT  /items` : Updates an existing item.
     *
     * @param item the item to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated item,
     * or with status `400 (Bad Request)` if the item is not valid,
     * or with status `500 (Internal Server Error)` if the item couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/items")
    fun updateItem(@Valid @RequestBody item: Item): ResponseEntity<Item> {
        log.debug("REST request to update Item : $item")
        if (item.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = itemService.save(item)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    item.id
                )
            )
            .body(result)
    }
    /**
     * `GET  /items` : get all the items.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of items in body.
     */
    @GetMapping("/items")
    fun getAllItems(pageable: Pageable): ResponseEntity<List<Item>> {
        log.debug("REST request to get a page of Items")
        val page = itemService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /items/:id` : get the "id" item.
     *
     * @param id the id of the item to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the item, or with status `404 (Not Found)`.
     */
    @GetMapping("/items/{id}")
    fun getItem(@PathVariable id: String): ResponseEntity<Item> {
        log.debug("REST request to get Item : $id")
        val item = itemService.findOne(id)
        return ResponseUtil.wrapOrNotFound(item)
    }
    /**
     *  `DELETE  /items/:id` : delete the "id" item.
     *
     * @param id the id of the item to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Item : $id")

        itemService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
