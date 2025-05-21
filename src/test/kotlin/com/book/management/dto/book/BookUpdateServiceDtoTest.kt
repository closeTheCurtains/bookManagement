package com.book.management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import java.math.BigDecimal
import kotlin.test.Test

class BookUpdateServiceDtoTest {

    @DisplayName("toEntityは Bookに変換されること")
    @Test
    fun `toEntity should convert DTO to correct Book`() {
        // given
        val dto = BookUpdateServiceDto(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = false,
            authorIds = listOf(1L)
        )

        // when
        val entity = dto.toEntity()

        // then
        assertThat(entity.id).isEqualTo(dto.id)
        assertThat(entity.title).isEqualTo(dto.title)
        assertThat(entity.price).isEqualTo(dto.price)
        assertThat(entity.publishStatus).isEqualTo(dto.publishStatus)
    }

}