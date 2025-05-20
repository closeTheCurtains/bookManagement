package com.book.management.dto.author

import com.book.management.anontion.PastDate
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class AuthorRequest(
    val id: Long? = null,

    @field:NotBlank(message = "苗字の入力は必須です。")
    val firstName: String,

    @field:NotBlank(message = "名前の入力は必須です。")
    val lastName: String,

    @field:NotBlank(message = "生年月日の入力は必須です。")
    @field:PastDate
    val birthDate: String
) {
    fun toRegisterServiceDto(): AuthorRegisterServiceDto {
        return AuthorRegisterServiceDto(
            firstName = firstName,
            lastName = lastName,
            birthDate = LocalDate.parse(birthDate)
        )
    }

    fun toUpdateServiceDto(): AuthorUpdateServiceDto = id?.let { authorId ->
        AuthorUpdateServiceDto(
            id = authorId,
            firstName = firstName,
            lastName = lastName,
            birthDate = LocalDate.parse(birthDate)
        )
    } ?: throw IllegalArgumentException("著者IDがnullです。")
}
