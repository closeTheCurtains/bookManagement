package com.book.management.controller

import com.book.management.dto.author.AuthorRequest
import com.book.management.generated.tables.pojos.Book
import com.book.management.service.AuthorService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/authors")
@RestController
class AuthorController(
    private val authorService: AuthorService
) {

    @GetMapping("/{authorId}/books")
    fun findBooksByAuthorId(@PathVariable authorId: Long): ResponseEntity<List<Book>> {
        return ResponseEntity.ok(authorService.findBooksByAuthorId(authorId))
    }

    @PostMapping
    fun register(@Valid @RequestBody request: AuthorRequest): ResponseEntity<Long> {
        val serviceDto = request.toRegisterServiceDto()
        return ResponseEntity.ok(authorService.register(serviceDto))
    }

    @PutMapping
    fun update(@Valid @RequestBody request: AuthorRequest): ResponseEntity<Long> {
        val serviceDto = request.toUpdateServiceDto()
        return ResponseEntity.ok(authorService.update(serviceDto))
    }
}