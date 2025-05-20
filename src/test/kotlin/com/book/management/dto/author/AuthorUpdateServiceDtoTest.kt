package com.book.management.dto.author

import com.book.management.generated.tables.pojos.Author
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AuthorUpdateServiceDtoTest {

    @DisplayName("toEntityは Authorに変換されること")
    @Test
    fun `toEntity should convert DTO to correct Author entity`() {
        // Given
        val dto = AuthorUpdateServiceDto(
            id = 1L,
            firstName = "firstName",
            lastName = "lastName",
            birthDate = LocalDate.of(1990, 1, 1)
        )

        // When
        val entity: Author = dto.toEntity()

        // Then
        assertThat(entity.id).isEqualTo(dto.id)
        assertThat(entity.firstName).isEqualTo(dto.firstName)
        assertThat(entity.lastName).isEqualTo(dto.lastName)
        assertThat(entity.birthDate).isEqualTo(dto.birthDate)
    }
}
