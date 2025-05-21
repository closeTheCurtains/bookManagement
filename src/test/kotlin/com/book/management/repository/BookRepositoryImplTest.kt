package com.book.management.repository

import com.book.management.generated.tables.pojos.Author
import com.book.management.generated.tables.pojos.Book
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.jooq.exception.NoDataFoundException
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
@Transactional
class BookRepositoryImplTest {

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @DisplayName("書籍IDで書籍検索 成功")
    @Test
    fun `findById success`() {
        // given
        val book = Book(
            title = "title",
            price = BigDecimal("500"),
            publishStatus = false,
        )
        val id = bookRepository.save(book)

        // when
        val searchBook = bookRepository.findById(id)

        //then
        assertThat(searchBook.id).isEqualTo(id)
    }

    @DisplayName("書籍IDで書籍検索(未登録) 失敗")
    @Test
    fun `findById fail`() {
        // given
        val id = 999L

        // when & then
        assertThatThrownBy {
            bookRepository.findById(id)
        }.isInstanceOf(NoDataFoundException::class.java)
            .hasMessage("該当書籍取得失敗。 id: $id")
    }

    @DisplayName("書籍登録 成功")
    @Test
    fun `save success`() {
        // given
        val book = Book(
            title = "title",
            price = BigDecimal("500"),
            publishStatus = false,
        )

        // when
        val id = bookRepository.save(book)

        // then
        assertThat(id).isNotNull()
    }

    @DisplayName("書籍更新 成功")
    @Test
    fun `update success`() {
        // given
        val newBook = Book(
            title = "title",
            price = BigDecimal("500"),
            publishStatus = false,
        )
        val newBookId = bookRepository.save(newBook)
        val updateBook = Book(
            id = newBookId,
            title = "title_update",
            price = BigDecimal("700"),
            publishStatus = true,
        )

        // when
        val updateBookId = bookRepository.update(updateBook)

        // then
        assertThat(updateBookId).isEqualTo(newBookId)
    }

    @DisplayName("書籍更新(未登録書籍) 失敗")
    @Test
    fun `update fail`() {
        // given
        val updateBook = Book(
            id = 999L,
            title = "title_update",
            price = BigDecimal("700"),
            publishStatus = true,
        )

        // when & then
        assertThatThrownBy {
            bookRepository.update(updateBook)
        }.isInstanceOf(NoDataFoundException::class.java)
            .hasMessage("該当書籍は未登録です。 id: ${updateBook.id}")
    }

    @DisplayName("著者IDで書籍リストを取得 成功")
    @Test
    fun `findBooksByAuthorId success`() {
        // given
        val book = Book(
            title = "title",
            price = BigDecimal("500"),
            publishStatus = false,
        )
        val author = Author(
            firstName = "test1",
            lastName = "test1",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val bookId = bookRepository.save(book)
        val authorId = authorRepository.register(author)
        val authorIds = listOf(authorId)
        bookRepository.assignAuthorsToBook(bookId, authorIds)

        // when
        val findBooks = bookRepository.findBooksByAuthorId(authorId)

        // then
        assertThat(findBooks[0].id).isEqualTo(bookId)
        assertThat(findBooks[0].title).isEqualTo(book.title)
        assertThat(findBooks[0].price).isEqualByComparingTo(book.price)
        assertThat(findBooks[0].publishStatus).isEqualTo(book.publishStatus)
    }

    @DisplayName("書籍IDで著者IDリストを取得 成功")
    @Test
    fun `findAuthorIdsByBookId success`() {
        // given
        val book = Book(
            title = "title",
            price = BigDecimal("500"),
            publishStatus = false,
        )
        val author1 = Author(
            firstName = "test1",
            lastName = "test1",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val author2 = Author(
            firstName = "test2",
            lastName = "test2",
            birthDate = LocalDate.of(1993, 11, 11),
        )
        val bookId = bookRepository.save(book)
        val author1Id = authorRepository.register(author1)
        val author2Id = authorRepository.register(author2)
        val authorIds = listOf(author1Id, author2Id)
        bookRepository.assignAuthorsToBook(bookId, authorIds)

        // when
        val result = bookRepository.findAuthorIdsByBookId(bookId)

        // then
        assertThat(result.size).isEqualTo(authorIds.size)
        assertThat(result[0]).isEqualTo(author1Id)
        assertThat(result[1]).isEqualTo(author2Id)
    }

    @DisplayName("著者と書籍紐付け登録 成功")
    @Test
    fun `assignAuthorsToBook success`() {
        // given
        val book = Book(
            title = "title",
            price = BigDecimal("500"),
            publishStatus = false,
        )
        val author1 = Author(
            firstName = "test1",
            lastName = "test1",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val author2 = Author(
            firstName = "test2",
            lastName = "test2",
            birthDate = LocalDate.of(1993, 11, 11),
        )
        val bookId = bookRepository.save(book)
        val author1Id = authorRepository.register(author1)
        val author2Id = authorRepository.register(author2)
        val authorIds = listOf(author1Id, author2Id)

        // when
        val result = bookRepository.assignAuthorsToBook(bookId, authorIds)

        // then
        assertThat(result).isEqualTo(2)

    }

    @DisplayName("書籍IDから紐づいだ著者と書籍を削除 成功")
    @Test
    fun `deleteBookAuthorLinkByBookId success`() {
        // given
        val book = Book(
            title = "title",
            price = BigDecimal("500"),
            publishStatus = false,
        )
        val author1 = Author(
            firstName = "test1",
            lastName = "test1",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val author2 = Author(
            firstName = "test2",
            lastName = "test2",
            birthDate = LocalDate.of(1993, 11, 11),
        )
        val bookId = bookRepository.save(book)
        val author1Id = authorRepository.register(author1)
        val author2Id = authorRepository.register(author2)
        val authorIds = listOf(author1Id, author2Id)
        val insertCount = bookRepository.assignAuthorsToBook(bookId, authorIds)

        // when
        val result = bookRepository.deleteBookAuthorLinkByBookId(bookId)

        // then
        assertThat(result).isEqualTo(insertCount)
    }
}