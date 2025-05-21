package com.book.management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import java.math.BigDecimal
import kotlin.test.Test

class BookSaveServiceDtoTest {

    @DisplayName("toEntityは Book に変換されること")
    @Test
    fun `toEntity should correctly convert to Book`() {
        // given
        val dto = BookSaveServiceDto(
            title = "BOOK A",
            price = BigDecimal.valueOf(1000),
            publishStatus = false,
            authorIds = listOf(1L)
        )

        // when
        val book = dto.toEntity()

        // then
        assertThat(book.title).isEqualTo(dto.title)
        assertThat(book.price).isEqualTo(dto.price)
        assertThat(book.publishStatus).isEqualTo(dto.publishStatus)
    }

}