package com.book.management.dto.author

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AuthorRequestTest {

    @DisplayName("toRegisterServiceDtoは AuthorRegisterServiceDtoに変換されること")
    @Test
    fun `toRegisterServiceDto should return correct service dto`() {
        // Given
        val request = AuthorRequest(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = "1990-01-01"
        )

        // When
        val dto = request.toRegisterServiceDto()

        // Then
        assertThat(dto.firstName).isEqualTo(request.firstName)
        assertThat(dto.lastName).isEqualTo(request.lastName)
        assertThat(dto.birthDate).isEqualTo(request.birthDate)
    }

    @DisplayName("toUpdateServiceDtoは idがある場合 AuthorUpdateServiceDtoに変換されること")
    @Test
    fun `toUpdateServiceDto should return correct service dto when id is not null`() {
        // Given
        val request = AuthorRequest(
            id = 1L,
            firstName = "firstName",
            lastName = "lastName",
            birthDate = "1990-01-01"
        )

        // When
        val dto = request.toUpdateServiceDto()

        // Then
        assertThat(dto.id).isEqualTo(request.id)
        assertThat(dto.firstName).isEqualTo(request.firstName)
        assertThat(dto.lastName).isEqualTo(request.lastName)
        assertThat(dto.toEntity().birthDate).isEqualTo(LocalDate.parse(request.birthDate))
    }

    @DisplayName("toUpdateServiceDtoは idがない場合 例外")
    @Test
    fun `toUpdateServiceDto should throw exception when id is null`() {
        // Given
        val request = AuthorRequest(
            firstName = "NoId",
            lastName = "User",
            birthDate = "2000-01-01"
        )

        // When & Then
        assertThatThrownBy {
            request.toUpdateServiceDto()
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("著者IDがnullです。")
    }
}
