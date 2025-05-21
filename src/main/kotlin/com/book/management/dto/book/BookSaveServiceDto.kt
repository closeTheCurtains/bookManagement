package com.book.management.dto.book

import com.book.management.generated.tables.pojos.Book
import java.math.BigDecimal

data class BookSaveServiceDto(
    val title: String,
    val price: BigDecimal,
    val publishStatus: Boolean,
    val authorIds: List<Long>
) {
    fun toEntity(): Book {
        return Book(
            title = title,
            price = price,
            publishStatus = publishStatus
        )
    }
}