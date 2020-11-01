package org.duttydev.financegrid.domain

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class BudgetTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Budget::class)
        val budget1 = Budget()
        budget1.id = "id1"
        val budget2 = Budget()
        budget2.id = budget1.id
        assertThat(budget1).isEqualTo(budget2)
        budget2.id = "id2"
        assertThat(budget1).isNotEqualTo(budget2)
        budget1.id = null
        assertThat(budget1).isNotEqualTo(budget2)
    }
}
