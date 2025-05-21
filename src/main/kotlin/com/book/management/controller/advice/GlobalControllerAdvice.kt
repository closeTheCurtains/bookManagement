package com.book.management.controller.advice

import org.jooq.exception.NoDataFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(IllegalArgumentException::class)
    fun invalidArgument(e: IllegalArgumentException): ResponseEntity<Map<String, String?>?> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("message" to e.message))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun invalidState(e: IllegalStateException): ResponseEntity<Map<String, String?>?> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("message" to e.message))
    }

    @ExceptionHandler(NoDataFoundException::class)
    fun noDataFound(e: NoDataFoundException): ResponseEntity<Map<String, String?>?> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to e.message))
    }
}