package org.duttydev.financegrid.domain

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class LoanTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Loan::class)
        val loan1 = Loan()
        loan1.id = "id1"
        val loan2 = Loan()
        loan2.id = loan1.id
        assertThat(loan1).isEqualTo(loan2)
        loan2.id = "id2"
        assertThat(loan1).isNotEqualTo(loan2)
        loan1.id = null
        assertThat(loan1).isNotEqualTo(loan2)
    }
}
