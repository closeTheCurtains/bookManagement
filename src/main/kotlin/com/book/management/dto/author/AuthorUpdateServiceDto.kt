package com.book.management.dto.author

import com.book.management.generated.tables.pojos.Author
import java.time.LocalDate

data class AuthorUpdateServiceDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate
) {
    fun toEntity(): Author {
        return Author(
            id = id,
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate
        )
    }
}