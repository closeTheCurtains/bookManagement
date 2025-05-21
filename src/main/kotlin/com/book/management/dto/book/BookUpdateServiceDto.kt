package com.book.management.dto.book

import com.book.management.generated.tables.pojos.Book
import java.math.BigDecimal

data class BookUpdateServiceDto(
    val id: Long,
    val title: String,
    val price: BigDecimal,
    val publishStatus: Boolean,
    val authorIds: List<Long>,
) {
    fun toEntity(): Book {
        return Book(
            id = id,
            title = title,
            price = price,
            publishStatus = publishStatus
        )
    }
}