package com.book.management.service

import com.book.management.dto.author.AuthorRegisterServiceDto
import com.book.management.dto.author.AuthorUpdateServiceDto
import com.book.management.generated.tables.pojos.Book
import com.book.management.repository.AuthorRepository
import com.book.management.repository.BookRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.jooq.exception.NoDataFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.time.LocalDate

class AuthorServiceTest {

    private lateinit var authorRepository: AuthorRepository
    private lateinit var bookRepository: BookRepository
    private lateinit var authorService: AuthorService

    @BeforeEach
    fun setUp() {
        authorRepository = mock(AuthorRepository::class.java)
        bookRepository = mock(BookRepository::class.java)
        authorService = AuthorService(authorRepository, bookRepository)
    }

    @DisplayName("著者登録")
    @Test
    fun `register should save author and return ID`() {
        // given
        val dto = AuthorRegisterServiceDto(
            "firstName",
            "lastName",
            LocalDate.of(1990, 1, 1)
        )
        val author = dto.toEntity()
        `when`(authorRepository.register(author)).thenReturn(1L)

        // when
        val result = authorService.register(dto)

        // then
        assertThat(result).isEqualTo(1L)
        verify(authorRepository).register(author)
    }

    @DisplayName("著者更新")
    @Test
    fun `update should update author and return ID`() {
        // given
        val dto = AuthorUpdateServiceDto(
            1L,
            "firstName",
            "lastName",
            LocalDate.of(1990, 1, 1)
        )
        val author = dto.toEntity()
        `when`(authorRepository.update(author)).thenReturn(1L)

        // when
        val result = authorService.update(dto)

        // then
        assertThat(result).isEqualTo(1L)
        verify(authorRepository).update(author)
    }

    @DisplayName("著者IDで書籍取得 成功")
    @Test
    fun `findBooksByAuthorId should return books when books exist`() {
        // given
        val authorId = 1L
        val books = listOf(
            Book(
                1,
                "Book A",
                BigDecimal("500")
            ), Book(
                2,
                "Book B",
                BigDecimal("500")
            )
        )
        `when`(bookRepository.findBooksByAuthorId(authorId)).thenReturn(books)

        // when
        val result = authorService.findBooksByAuthorId(authorId)

        // then
        assertThat(result).hasSize(2)
        verify(bookRepository).findBooksByAuthorId(1L)
    }

    @DisplayName("著者で書籍取得 失敗")
    @Test
    fun `findBooksByAuthorId should throw NoDataFoundException when no books exist`() {
        // given
        `when`(bookRepository.findBooksByAuthorId(99L)).thenReturn(emptyList())

        // when & then
        assertThatThrownBy {
            authorService.findBooksByAuthorId(99L)
        }.isInstanceOf(NoDataFoundException::class.java)
            .hasMessage("著者にて登録された書籍はありません。")
    }
}
