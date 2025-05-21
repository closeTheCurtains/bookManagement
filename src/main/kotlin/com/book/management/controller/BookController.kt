package com.book.management.controller

import com.book.management.dto.book.BookRequest
import com.book.management.generated.tables.pojos.Book
import com.book.management.service.BookService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/books")
@RestController
class BookController(
    private val bookService: BookService,
) {

    @PostMapping
    fun save(@Valid @RequestBody request: BookRequest): ResponseEntity<Long> {
        val serviceDto = request.toSaveServiceDto()
        return ResponseEntity.ok(bookService.save(serviceDto))
    }

    @PutMapping
    fun update(@Valid @RequestBody request: BookRequest): ResponseEntity<Long> {
        val serviceDto = request.toUpdateServiceDto()
        return ResponseEntity.ok(bookService.update(serviceDto))
    }
}