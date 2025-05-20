package com.book.management.repository

import com.book.management.generated.tables.pojos.Author
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.jooq.exception.NoDataFoundException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class AuthorRepositoryImplTest {

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @DisplayName("著者登録 成功")
    @Test
    fun `register success`() {
        // given
        val author = Author(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = LocalDate.of(1990, 1, 1),
        )

        // when
        val id = authorRepository.register(author)

        // then
        assertThat(id).isNotNull()
    }

    @DisplayName("著者更新 成功")
    @Test
    fun `update success`() {
        // given
        val newAuthor = Author(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val newAuthorId = authorRepository.register(newAuthor)
        val updatedAuthor = Author(
            id = newAuthorId,
            firstName = "firstName_updated",
            lastName = "lastName_updated",
            birthDate = LocalDate.of(1990, 12, 31),
        )

        // when
        val updatedAuthorId = authorRepository.update(updatedAuthor)

        // then
        assertThat(updatedAuthorId).isEqualTo(newAuthorId)

    }

    @DisplayName("著者更新(未登録著者) 失敗")
    @Test
    fun `update fail`() {
        // given
        val updateAuthor = Author(
            id = 999999L,
            firstName = "firstName_updated",
            lastName = "lastName_updated",
            birthDate = LocalDate.of(1990, 12, 31),
        )

        // when & then
        assertThatThrownBy {
            authorRepository.update(updateAuthor)
        }.isInstanceOf(NoDataFoundException::class.java)
            .hasMessage("該当著者は未登録です。 id: 999999")

    }

    @DisplayName("著者全部登録済み")
    @Test
    fun `existsAuthorByIds return true success`() {
        // given
        val author1 = Author(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val author2 = Author(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val authorId1 = authorRepository.register(author1)
        val authorId2 = authorRepository.register(author2)
        val authorIds = listOf(authorId1, authorId2)

        // when
        val result = authorRepository.existsAuthorByIds(authorIds)

        // then
        assertThat(result).isTrue()
    }


    @DisplayName("著者未登録あり")
    @Test
    fun `existsAuthorByIds return false success`() {
        // given
        val author1 = Author(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val author2 = Author(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = LocalDate.of(1990, 1, 1),
        )
        val authorId1 = authorRepository.register(author1)
        val authorId2 = authorRepository.register(author2)
        val authorId3 = 999999L
        val authorIds = listOf(authorId1, authorId2, authorId3)

        // when
        val result = authorRepository.existsAuthorByIds(authorIds)

        // then
        assertThat(result).isFalse()
    }
}
