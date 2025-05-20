package com.book.management.service

import com.book.management.dto.author.AuthorRegisterServiceDto
import com.book.management.dto.author.AuthorUpdateServiceDto
import com.book.management.generated.tables.pojos.Book
import com.book.management.repository.AuthorRepository
import com.book.management.repository.BookRepository
import org.jooq.exception.NoDataFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
) {
    fun register(dto: AuthorRegisterServiceDto): Long {
        val author = dto.toEntity()
        return authorRepository.register(author)
    }

    fun update(dto: AuthorUpdateServiceDto): Long {
        val author = dto.toEntity()
        return authorRepository.update(author)
    }

    fun findBooksByAuthorId(authorId: Long): List<Book> {
        val books = bookRepository.findBooksByAuthorId(authorId)
        if (books.isEmpty()) {
            throw NoDataFoundException("著者にて登録された書籍はありません。")
        }
        return books
    }
}