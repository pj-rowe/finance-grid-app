package org.duttydev.financegrid.repository

import org.duttydev.financegrid.domain.Item
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Item] entity.
 */
@Suppress("unused")
@Repository
interface ItemRepository : MongoRepository<Item, String>
