package org.duttydev.financegrid.domain

import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.*

/**
 * A LoanPrincipal.
 */
// @Document(collection = "loan_principal")
data class LoanPrincipal(
//    @Id
//    var id: String? = null,
    @get: NotNull
    @Field("date")
    var date: LocalDate? = null,

    @get: NotNull
    @Field("amount")
    var amount: BigDecimal? = null

//    @DBRef
//    @Field("loan")
//    @JsonIgnoreProperties(value = ["loanPrincipals"], allowSetters = true)
//    var loan: Loan? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LoanPrincipal) return false

        return date != null && other.date != null && date == other.date
    }

    override fun hashCode() = 31

    override fun toString() = "LoanPrincipal{" +
        "date='$date'" +
        ", amount=$amount" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
