package org.duttydev.financegrid.domain

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class LoanPrincipalTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(LoanPrincipal::class)
        val loanPrincipal1 = LoanPrincipal()
        loanPrincipal1.id = "id1"
        val loanPrincipal2 = LoanPrincipal()
        loanPrincipal2.id = loanPrincipal1.id
        assertThat(loanPrincipal1).isEqualTo(loanPrincipal2)
        loanPrincipal2.id = "id2"
        assertThat(loanPrincipal1).isNotEqualTo(loanPrincipal2)
        loanPrincipal1.id = null
        assertThat(loanPrincipal1).isNotEqualTo(loanPrincipal2)
    }
}
