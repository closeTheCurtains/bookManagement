package com.book.management.service

import com.book.management.dto.book.BookSaveServiceDto
import com.book.management.dto.book.BookUpdateServiceDto
import com.book.management.generated.tables.pojos.Book
import com.book.management.repository.AuthorRepository
import com.book.management.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
) {

    fun save(dto: BookSaveServiceDto): Long {
        val book = dto.toEntity()

        // 著者が未登録の場合、例外
        if (!authorRepository.existsAuthorByIds(dto.authorIds)) {
            throw IllegalStateException("未著者があります。 id: ${dto.authorIds}")
        }

        val saveBookId = bookRepository.save(book)

        // 書籍に紐づく著者を登録
        bookRepository.assignAuthorsToBook(saveBookId, dto.authorIds)

        return saveBookId
    }

    fun update(dto: BookUpdateServiceDto): Long {
        val book = dto.toEntity()

        // DBにある書籍を取得
        val currentBook = bookRepository.findById(book.id!!)

        // DB上出版済みで更新する書籍が未出版にする場合、例外
        if (currentBook.publishStatus == true && book.publishStatus == false) {
            throw IllegalStateException("出版済み書籍は出版状況を更新できません。")
        }

        // 書籍IDで書籍に紐づいている著者IDリストを取得
        val authorIds = bookRepository.findAuthorIdsByBookId(book.id!!)

        // dtoの著者IDリストと取得した著者IDリストを比較
        // 一致しない場合、book_author_linkテーブルにあるbookIdを削除後
        // book_author_linkテーブルに書籍と著者関係を登録
        if (dto.authorIds.sorted() != authorIds) {
            bookRepository.deleteBookAuthorLinkByBookId(book.id!!)
            bookRepository.assignAuthorsToBook(book.id!!, dto.authorIds)
        }

        return bookRepository.update(book)
    }


}