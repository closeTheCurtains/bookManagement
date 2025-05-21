package com.book.management.repository

import com.book.management.generated.tables.pojos.Book

/**
 * BookRepository interface
 */
interface BookRepository {
    // 書籍IDで書籍を取得
    fun findById(id: Long): Book

    // 書籍登録
    fun save(book: Book): Long

    // 書籍更新
    fun update(book: Book): Long

    // 著者IDで書籍リストを取得
    fun findBooksByAuthorId(authorId: Long): List<Book>

    // 書籍IDで著者IDリストを取得
    fun findAuthorIdsByBookId(bookId: Long): List<Long>

    // 著者と書籍紐付け登録
    fun assignAuthorsToBook(bookId: Long, authorIds: List<Long>): Int

    // 書籍IDから紐づいだ著者と書籍を削除
    fun deleteBookAuthorLinkByBookId(bookId: Long): Int
}