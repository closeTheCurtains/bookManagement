package com.book.management.dto.book

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class BookRequest(
    val id: Long? = null,

    @field:NotBlank(message = "タイトルの入力は必須です。")
    val title: String,

    @field:NotNull(message = "価格の入力は必須です。")
    @field:DecimalMin(value = "0.0", message = "0.0以上を入力してください。")
    val price: BigDecimal,

    @field:NotNull(message = "出版状況の入力は必須です。")
    val publishStatus: Boolean,

    @field:Size(min = 1, message = "著者は１名以上入力してください。")
    val authorIds: List<Long>
) {
    fun toSaveServiceDto(): BookSaveServiceDto {
        return BookSaveServiceDto(
            title = title,
            price = price,
            publishStatus = publishStatus,
            authorIds = authorIds,
        )
    }

    fun toUpdateServiceDto(): BookUpdateServiceDto = id?.let { bookId ->
        BookUpdateServiceDto(
            id = bookId,
            title = title,
            price = price,
            publishStatus = publishStatus,
            authorIds = authorIds
        )
    } ?: throw IllegalArgumentException("書籍IDがnullです。")
}