package com.book.management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import java.math.BigDecimal
import kotlin.test.Test

class BookRequestTest {

    @DisplayName("toSaveServiceDtoは BookSaveServiceDtoに変換されること")
    @Test
    fun `toSaveServiceDto should return a BookSaveServiceDto with correct values`() {
        // given
        val request = BookRequest(
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = true,
            authorIds = listOf(1L, 2L)
        )

        // when
        val dto = request.toSaveServiceDto()

        // then
        assertThat(dto.title).isEqualTo(request.title)
        assertThat(dto.price).isEqualTo(request.price)
        assertThat(dto.publishStatus).isEqualTo(request.publishStatus)
        assertThat(dto.authorIds).isEqualTo(request.authorIds)
    }

    @DisplayName("toUpdateServiceDtoは idがある場合 BookUpdateServiceDtoに変換されること")
    @Test
    fun `toUpdateServiceDto should return a BookUpdateServiceDto with correct values`() {
        // given
        val request = BookRequest(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = true,
            authorIds = listOf(1L, 2L)
        )

        // when
        val dto = request.toUpdateServiceDto()

        // then
        assertThat(dto.id).isEqualTo(request.id)
        assertThat(dto.title).isEqualTo(request.title)
        assertThat(dto.price).isEqualTo(request.price)
        assertThat(dto.publishStatus).isEqualTo(request.publishStatus)
        assertThat(dto.authorIds).isEqualTo(request.authorIds)
    }

    @DisplayName("toUpdateServiceDtoは idがない場合 例外")
    @Test
    fun `toUpdateServiceDto should throw exception when id is null`() {
        // given
        val request = BookRequest(
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = false,
            authorIds = listOf(1L, 2L)
        )

        // when + then
        assertThatThrownBy {
            request.toUpdateServiceDto()
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("書籍IDがnullです。")
    }
}