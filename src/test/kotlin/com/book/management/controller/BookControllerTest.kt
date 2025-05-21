package com.book.management.controller

import com.book.management.dto.book.BookRequest
import com.book.management.service.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.math.BigDecimal

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var bookService: BookService

    @DisplayName("書籍登録し書籍ID返却")
    @Test
    fun `POST save should return new book ID`() {
        // given
        val request = BookRequest(
            id = null,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = false,
            authorIds = listOf(1L, 2L)
        )
        val dto = request.toSaveServiceDto()
        given(bookService.save(dto)).willReturn(1L)

        // when & then
        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { string("1") }
        }
    }

    @DisplayName("書籍更新し書籍ID返却")
    @Test
    fun `PUT update should return book ID`() {
        // given
        val request = BookRequest(
            id = 1L,
            title = "BOOK A",
            price = BigDecimal(1000),
            publishStatus = true,
            authorIds = listOf(1L, 2L)
        )
        val dto = request.toUpdateServiceDto()
        given(bookService.update(dto)).willReturn(1L)

        // when & then
        mockMvc.put("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { string("1") }
        }
    }
}
