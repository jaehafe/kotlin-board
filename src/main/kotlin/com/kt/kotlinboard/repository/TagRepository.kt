package com.kt.kotlinboard.repository

import com.kt.kotlinboard.domain.Tag
import org.springframework.data.domain.Page
import com.kt.kotlinboard.domain.QTag.tag
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface TagRepository : JpaRepository<Tag, Long>, CustomTagRepository {
    fun findByPostId(postId: Long): List<Tag>
}

interface CustomTagRepository {
    fun findPageBy(pageRequest: Pageable, tagName: String): Page<Tag>
}

class CustomTagRepositoryImpl : CustomTagRepository, QuerydslRepositorySupport(Tag::class.java) {

    override fun findPageBy(pageRequest: Pageable, tagName: String): Page<Tag> {
        return from(tag)
            .join(tag.post).fetchJoin()
            .where(tag.name.eq(tagName))
            .orderBy(tag.post.createdAt.desc())
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .fetchResults()
            .let { PageImpl(it.results, pageRequest, it.total) }
    }

}
