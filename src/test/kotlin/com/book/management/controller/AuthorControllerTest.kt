package com.book.management.controller

import com.book.management.dto.author.AuthorRequest
import com.book.management.generated.tables.pojos.Book
import com.book.management.service.AuthorService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(AuthorController::class)
class AuthorControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var authorService: AuthorService

    @DisplayName("著者IDで書籍リストを取得")
    @Test
    fun `GET books by authorId should return list of books`() {
        // Given
        val authorId = 1L
        val books = listOf(
            Book(1, "Book A"),
            Book(2, "Book B")
        )
        given(authorService.findBooksByAuthorId(authorId)).willReturn(books)

        // When + Then
        mockMvc.get("/authors/{authorId}/books", authorId)
            .andExpect {
                status { isOk() }
                jsonPath("$.size()") { value(2) }
                jsonPath("$[0].id") { value(1L) }
                jsonPath("$[0].title") { value("Book A") }
                jsonPath("$[1].id") { value(2L) }
                jsonPath("$[1].title") { value("Book B") }
            }
    }

    @DisplayName("著者登録し著者ID返却")
    @Test
    fun `POST register should return new author ID`() {
        // Given
        val request = AuthorRequest(
            firstName = "firstName",
            lastName = "lastName",
            birthDate = "1990-01-01"
        )
        val dto = request.toRegisterServiceDto()
        given(authorService.register(dto)).willReturn(10L)

        // When + Then
        mockMvc.post("/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { string("10") }
        }
    }

    @DisplayName("著者更新し著者ID返却")
    @Test
    fun `PUT update should return author ID`() {
        // Given
        val request = AuthorRequest(
            id = 10L,
            firstName = "firstName",
            lastName = "lastName",
            birthDate = "1990-01-01"
        )
        val dto = request.toUpdateServiceDto()
        given(authorService.update(dto)).willReturn(10L)

        // When + Then
        mockMvc.put("/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { string("10") }
        }
    }
}
