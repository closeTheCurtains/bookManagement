package com.book.management.anontion

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

/**
 * 未来日付かを検証するアノテーション
 */
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [PastDateValidator::class])
annotation class PastDate(
    val message: String = "日付は過去でなければなりません。",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

/**
 * valueがシステム日付より過去の場合true
 * valueがシステム日付より未来の場合false
 */
class PastDateValidator : ConstraintValidator<PastDate, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true
        val date = LocalDate.parse(value)
        return date.isBefore(LocalDate.now())
    }

}
