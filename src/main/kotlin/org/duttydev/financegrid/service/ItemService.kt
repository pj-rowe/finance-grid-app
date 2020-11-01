package org.duttydev.financegrid.service
import org.duttydev.financegrid.domain.Item
import org.duttydev.financegrid.repository.ItemRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Service Implementation for managing [Item].
 */
@Service
class ItemService(
    private val itemRepository: ItemRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a item.
     *
     * @param item the entity to save.
     * @return the persisted entity.
     */
    fun save(item: Item): Item {
        log.debug("Request to save Item : $item")
        return itemRepository.save(item)
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<Item> {
        log.debug("Request to get all Items")
        return itemRepository.findAll(pageable)
    }

    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Item> {
        log.debug("Request to get Item : $id")
        return itemRepository.findById(id)
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Item : $id")

        itemRepository.deleteById(id)
    }
}
