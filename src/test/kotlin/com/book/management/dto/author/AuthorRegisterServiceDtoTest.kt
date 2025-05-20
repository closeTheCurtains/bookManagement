package com.book.management.dto.author

import com.book.management.generated.tables.pojos.Author
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AuthorRegisterServiceDtoTest {

    @DisplayName("toEntityは Author に変換されること")
    @Test
    fun `toEntity should correctly convert to Author`() {
        // given
        val firstName = "firstName"
        val lastName = "lastName"
        val birthDate = LocalDate.of(1990, 1, 1)

        val dto = AuthorRegisterServiceDto(
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate
        )

        // when
        val author: Author = dto.toEntity()

        // then
        assertThat(author.firstName).isEqualTo(firstName)
        assertThat(author.lastName).isEqualTo(lastName)
        assertThat(author.birthDate).isEqualTo(birthDate)
    }
}
