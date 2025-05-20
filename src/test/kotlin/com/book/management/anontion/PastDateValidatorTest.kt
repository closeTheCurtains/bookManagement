package com.book.management.anontion

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeParseException

class PastDateValidatorTest {

    private val validator = PastDateValidator()

    @DisplayName("過去日付なら true を返す")
    @Test
    fun `should return true for past date`() {
        val value = LocalDate.now().minusDays(1).toString()
        val result = validator.isValid(value, null)
        assertThat(result).isTrue()
    }

    @DisplayName("未来日付なら false を返す")
    @Test
    fun `should return false for future date`() {
        val value = LocalDate.now().plusDays(1).toString()
        val result = validator.isValid(value, null)
        assertThat(result).isFalse()
    }

    @DisplayName("null は有効とみなされ true を返す")
    @Test
    fun `should return true for null value`() {
        val result = validator.isValid(null, null)
        assertThat(result).isTrue()
    }

    @DisplayName("フォーマット不正な文字列は例外を投げる")
    @Test
    fun `should throw exception for invalid format`() {
        val invalidDate = "not-a-date"
        assertThatThrownBy {
            validator.isValid(invalidDate, null)
        }.isInstanceOf(DateTimeParseException::class.java)
    }
}
