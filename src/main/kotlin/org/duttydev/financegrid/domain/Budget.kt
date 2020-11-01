package org.duttydev.financegrid.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import javax.validation.constraints.*

/**
 * A Budget.
 */
@Document(collection = "budget")
data class Budget(
    @Id
    var id: String? = null,
    @get: NotNull
    @get: Size(min = 3)
    @Field("name")
    var name: String? = null,

    @DBRef
    @Field("items")
    var items: MutableSet<Item> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    fun addItems(item: Item): Budget {
        this.items.add(item)
        item.budget = this
        return this
    }

    fun removeItems(item: Item): Budget {
        this.items.remove(item)
        item.budget = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Budget) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Budget{" +
        "id=$id" +
        ", name='$name'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
