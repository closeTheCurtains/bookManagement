package com.book.management.service

import com.book.management.dto.book.BookSaveServiceDto
import com.book.management.dto.book.BookUpdateServiceDto
import com.book.management.generated.tables.pojos.Book
import com.book.management.generated.tables.pojos.BookAuthorLink
import com.book.management.repository.AuthorRepository
import com.book.management.repository.BookRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.kotlin.any
import java.math.BigDecimal

class BookServiceTest {

    private lateinit var bookRepository: BookRepository
    private lateinit var authorRepository: AuthorRepository
    private lateinit var bookService: BookService

    @BeforeEach
    fun setup() {
        bookRepository = mock(BookRepository::class.java)
        authorRepository = mock(AuthorRepository::class.java)
        bookService = BookService(bookRepository, authorRepository)
    }

    @DisplayName("書籍登録")
    @Test
    fun `save should return Book ID`() {
        // given
        val dto = BookSaveServiceDto(
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = false,
            authorIds = listOf(1L, 2L)
        )
        val book = dto.toEntity()
        given(authorRepository.existsAuthorByIds(dto.authorIds)).willReturn(true)
        given(bookRepository.save(book)).willReturn(1L)

        // when
        val result = bookService.save(dto)

        // then
        assertThat(result).isEqualTo(1L)
        verify(authorRepository).existsAuthorByIds(dto.authorIds)
        verify(bookRepository).save(book)
    }

    @DisplayName("書籍登録 失敗")
    @Test
    fun `save should throw IllegalStateException when authorIds is empty`() {
        // given
        val dto = BookSaveServiceDto(
            title = "BOOK B",
            price = BigDecimal(1000),
            publishStatus = false,
            authorIds = listOf(99L)
        )
        given(authorRepository.existsAuthorByIds(dto.authorIds)).willReturn(false)

        // when & then
        assertThatThrownBy {
            bookService.save(dto)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessage("未著者があります。 id: ${dto.authorIds}")
    }

    @DisplayName("書籍更新(同一著者) 成功")
    @Test
    fun `update should update book and return Id when same author`() {
        // given
        val dto = BookUpdateServiceDto(
            id = 1L,
            title = "BOOK A update",
            price = BigDecimal(2000),
            publishStatus = true,
            authorIds = listOf(1L)
        )
        val updateBook = dto.toEntity()
        val existingBook = Book(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = true,
        )
        val existingBookAuthorLink = BookAuthorLink(
            bookId = 1L,
            authorId = 1L
        )

        given(bookRepository.findById(updateBook.id!!)).willReturn(existingBook)
        given(bookRepository.findAuthorIdsByBookId(updateBook.id!!))
            .willReturn(listOf(existingBookAuthorLink.authorId!!))
        given(bookRepository.update(updateBook)).willReturn(existingBook.id)

        // when
        val result = bookService.update(dto)

        // then
        assertThat(result).isEqualTo(1L)
        verify(bookRepository).findById(1L)
        verify(bookRepository).findAuthorIdsByBookId(1L)
        verify(bookRepository).update(updateBook)
        verify(bookRepository, never()).deleteBookAuthorLinkByBookId(any())
        verify(bookRepository, never()).assignAuthorsToBook(any(), any())
    }

    @DisplayName("書籍更新(著者変更) 成功")
    @Test
    fun `update should update book and return Id when changed author`() {
        // given
        val dto = BookUpdateServiceDto(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = true,
            authorIds = listOf(2L)
        )
        val updateBook = dto.toEntity()
        val existingBook = Book(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = true,
        )
        val existingBookAuthorLink = BookAuthorLink(
            bookId = 1L,
            authorId = 1L
        )

        given(bookRepository.findById(updateBook.id!!)).willReturn(existingBook)
        given(bookRepository.findAuthorIdsByBookId(updateBook.id!!))
            .willReturn(listOf(existingBookAuthorLink.authorId!!))
        given(bookRepository.update(updateBook)).willReturn(1L)

        // when
        val result = bookService.update(dto)

        // then
        assertThat(result).isEqualTo(1L)
        verify(bookRepository).deleteBookAuthorLinkByBookId(1L)
        verify(bookRepository).assignAuthorsToBook(1L, listOf(2L))
    }

    @DisplayName("書籍更新(出版済み → 未出版) 失敗")
    @Test
    fun `update should throw IllegalStateException when publishStatus ture to false`() {
        // given
        val dto = BookUpdateServiceDto(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = false,
            authorIds = listOf(1L)
        )
        val updateBook = dto.toEntity()
        val existingBook = Book(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = true,
        )

        given(bookRepository.findById(updateBook.id!!)).willReturn(existingBook)

        // when & then
        assertThatThrownBy{
            bookService.update(dto)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessage("出版済み書籍は出版状況を更新できません。")
    }
}
