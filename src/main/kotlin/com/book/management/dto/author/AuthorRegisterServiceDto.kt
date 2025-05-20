package com.book.management.dto.author

import com.book.management.generated.tables.pojos.Author
import java.time.LocalDate

data class AuthorRegisterServiceDto(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate
) {
    fun toEntity(): Author {
        return Author(
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate
        )
    }
}