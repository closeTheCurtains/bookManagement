package com.book.management.repository

import com.book.management.generated.tables.pojos.Book
import com.book.management.generated.tables.references.BOOK
import com.book.management.generated.tables.references.BOOK_AUTHOR_LINK
import org.jooq.DSLContext
import org.jooq.exception.NoDataFoundException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * 書籍レポジトリ
 * BookRepository実装クラス
 */
@Repository
class BookRepositoryImpl(private val dslContext: DSLContext) : BookRepository {

    /**
     * 書籍IDで書籍を取得
     *
     * @param id 書籍ID
     * @return 書籍
     * @throws NoDataFoundException
     */
    override fun findById(id: Long): Book {
        return dslContext.selectFrom(BOOK)
            .where(BOOK.ID.eq(id))
            .fetchOneInto(Book::class.java)
            ?: throw NoDataFoundException("該当書籍取得失敗。 id: $id")
    }

    /**
     * 書籍登録
     *
     * @param book 書籍
     * @return 登録後ID
     * @throws IllegalArgumentException
     */
    override fun save(book: Book): Long {
        return dslContext.insertInto(
            BOOK,
            BOOK.TITLE,
            BOOK.PRICE,
            BOOK.PUBLISH_STATUS
        ).values(
            book.title,
            book.price,
            book.publishStatus
        ).returningResult(BOOK.ID)
            .fetchOneInto(Long::class.java)
            ?: error("書籍登録失敗　IDがリターンされてない")
    }

    /**
     * 書籍更新
     *
     * @param book 書籍
     * @return 書籍ID
     * @throws NoDataFoundException
     */
    override fun update(book: Book): Long {
        val updatedBookRow = dslContext.update(BOOK)
            .set(BOOK.TITLE, book.title)
            .set(BOOK.PRICE, book.price)
            .set(BOOK.PUBLISH_STATUS, book.publishStatus)
            .where(BOOK.ID.eq(book.id))
            .execute()

        return if (updatedBookRow > 0) {
            book.id!!
        } else {
            throw NoDataFoundException("該当書籍は未登録です。 id: ${book.id}")
        }
    }

    /**
     * 著者IDで書籍リストを取得
     *
     * @param authorId 著者ID
     * @return 書籍リスト
     */
    override fun findBooksByAuthorId(authorId: Long): List<Book> {
        return dslContext.select(
            BOOK.ID,
            BOOK.TITLE,
            BOOK.PRICE,
            BOOK.PUBLISH_STATUS
        ).from(BOOK)
            .join(BOOK_AUTHOR_LINK)
            .on(BOOK.ID.eq(BOOK_AUTHOR_LINK.BOOK_ID))
            .where(BOOK_AUTHOR_LINK.AUTHOR_ID.eq(authorId))
            .fetchInto(Book::class.java)
    }

    /**
     * 書籍IDで著者IDリストを取得
     *
     * @param bookId 書籍ID
     * @return 著者IDリスト(昇順ソート)
     */
    override fun findAuthorIdsByBookId(bookId: Long): List<Long> {
        return dslContext.select(BOOK_AUTHOR_LINK.AUTHOR_ID)
            .from(BOOK_AUTHOR_LINK)
            .where(BOOK_AUTHOR_LINK.BOOK_ID.eq(bookId))
            .orderBy(BOOK_AUTHOR_LINK.AUTHOR_ID.asc())
            .fetchInto(Long::class.java)
    }

    /**
     * 著者と書籍紐付け登録
     *
     * @param bookId 書籍ID
     * @param authorIds 著者IDリスト
     */
    @Transactional
    override fun assignAuthorsToBook(bookId: Long, authorIds: List<Long>): Int {
        return authorIds.sumOf { authorId ->
            dslContext.insertInto(
                BOOK_AUTHOR_LINK,
                BOOK_AUTHOR_LINK.BOOK_ID,
                BOOK_AUTHOR_LINK.AUTHOR_ID
            )
                .values(bookId, authorId)
                .execute()
        }
    }

    /**
     * 書籍IDから紐づいだ著者と書籍を削除
     *
     * @param bookId 書籍ID
     * @return 削除件数
     */
    override fun deleteBookAuthorLinkByBookId(bookId: Long): Int {
        return dslContext.deleteFrom(BOOK_AUTHOR_LINK)
            .where(BOOK_AUTHOR_LINK.BOOK_ID.eq(bookId))
            .execute()
    }
}