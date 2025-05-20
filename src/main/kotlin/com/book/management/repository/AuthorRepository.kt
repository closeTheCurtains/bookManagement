package com.book.management.repository

import com.book.management.generated.tables.pojos.Author

/**
 * AuthorRepository interface
 */
interface AuthorRepository {
    // 著者登録
    fun register(author: Author): Long

    // 著者更新
    fun update(author: Author): Long

    // 著者IDリストにあるものが全部登録済みであるか
    fun existsAuthorByIds(authorIds: List<Long>): Boolean
}