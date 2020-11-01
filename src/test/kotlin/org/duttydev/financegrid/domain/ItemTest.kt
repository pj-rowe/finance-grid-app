package org.duttydev.financegrid.domain

import org.assertj.core.api.Assertions.assertThat
import org.duttydev.financegrid.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class ItemTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Item::class)
        val item1 = Item()
        item1.id = "id1"
        val item2 = Item()
        item2.id = item1.id
        assertThat(item1).isEqualTo(item2)
        item2.id = "id2"
        assertThat(item1).isNotEqualTo(item2)
        item1.id = null
        assertThat(item1).isNotEqualTo(item2)
    }
}
