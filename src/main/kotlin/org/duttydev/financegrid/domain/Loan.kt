package org.duttydev.financegrid.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import javax.validation.constraints.*

/**
 * A Loan.
 */
@Document(collection = "loan")
data class Loan(
    @Id
    var id: String? = null,
    @get: NotNull
    @get: Size(min = 3)
    @Field("name")
    var name: String? = null,

    @DBRef
    @Field("loanPrincipals")
    var loanPrincipals: MutableSet<LoanPrincipal> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    fun addLoanPrincipals(loanPrincipal: LoanPrincipal): Loan {
        this.loanPrincipals.add(loanPrincipal)
        loanPrincipal.loan = this
        return this
    }

    fun removeLoanPrincipals(loanPrincipal: LoanPrincipal): Loan {
        this.loanPrincipals.remove(loanPrincipal)
        loanPrincipal.loan = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Loan) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Loan{" +
        "id=$id" +
        ", name='$name'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
