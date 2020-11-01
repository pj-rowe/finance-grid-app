package org.duttydev.financegrid.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.duttydev.financegrid.domain.enumeration.ItemCategory
import org.duttydev.financegrid.domain.enumeration.ItemType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.*

/**
 * A Item.
 */
@Document(collection = "item")
data class Item(
    @Id
    var id: String? = null,
    @get: NotNull
    @get: Size(min = 3)
    @Field("name")
    var name: String? = null,

    @get: NotNull
    @Field("item_type")
    var itemType: ItemType? = null,

    @Field("due_date")
    var dueDate: LocalDate? = null,

    @Field("paid")
    var paid: Boolean? = null,

    @get: NotNull
    @Field("expected_amount")
    var expectedAmount: BigDecimal? = null,

    @get: NotNull
    @Field("actual_amount")
    var actualAmount: BigDecimal? = null,

    @Field("category")
    var category: ItemCategory? = null,

    @DBRef
    @Field("budget")
    @JsonIgnoreProperties(value = ["items"], allowSetters = true)
    var budget: Budget? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Item{" +
        "id=$id" +
        ", name='$name'" +
        ", itemType='$itemType'" +
        ", dueDate='$dueDate'" +
        ", paid='$paid'" +
        ", expectedAmount=$expectedAmount" +
        ", actualAmount=$actualAmount" +
        ", category='$category'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
