package com.book.management.repository

import com.book.management.generated.tables.pojos.Author
import com.book.management.generated.tables.references.AUTHOR
import org.jooq.DSLContext
import org.jooq.exception.NoDataFoundException
import org.springframework.stereotype.Repository

/**
 * 著者レポジトリ
 * AuthorRepository実装クラス
 */
@Repository
class AuthorRepositoryImpl(private val dslContext: DSLContext) : AuthorRepository {

    /**
     * 著者登録
     *
     * @param author 著者
     * @return 登録後ID
     * @throws IllegalArgumentException
     */
    override fun register(author: Author): Long {
        return dslContext.insertInto(
            AUTHOR,
            AUTHOR.FIRST_NAME,
            AUTHOR.LAST_NAME,
            AUTHOR.BIRTH_DATE
        ).values(
            author.firstName,
            author.lastName,
            author.birthDate,
        ).returningResult(AUTHOR.ID)
            .fetchOneInto(Long::class.java)
            ?: error("著者登録失敗　IDがリターンされてない")
    }

    /**
     * 著者更新
     *
     * @param author 著者
     * @return 著者ID
     * @throws NoDataFoundException
     */
    override fun update(author: Author): Long {
        val updateAuthorRow = dslContext.update(AUTHOR)
            .set(AUTHOR.FIRST_NAME, author.firstName)
            .set(AUTHOR.LAST_NAME, author.lastName)
            .set(AUTHOR.BIRTH_DATE, author.birthDate)
            .where(AUTHOR.ID.eq(author.id))
            .execute()

        return if (updateAuthorRow > 0) {
            author.id!!
        } else {
            throw NoDataFoundException("該当著者は未登録です。 id: ${author.id}")
        }
    }

    /**
     * 著者IDリストにあるものが全部登録済みであるか
     *
     * @param authorIds 著者IDリスト
     * @return true:登録済み、false:未登録あり
     */
    override fun existsAuthorByIds(authorIds: List<Long>): Boolean {
        val count = dslContext.selectCount()
            .from(AUTHOR)
            .where(AUTHOR.ID.`in`(authorIds))
            .fetchOne(0, Int::class.java)

        return count == authorIds.size
    }


}