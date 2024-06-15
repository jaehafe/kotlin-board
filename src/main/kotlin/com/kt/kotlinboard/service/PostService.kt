package com.kt.kotlinboard.service

import com.kt.kotlinboard.controller.dto.post.response.PostDetailResponseDto
import com.kt.kotlinboard.controller.dto.post.response.toDetailResponseDto
import com.kt.kotlinboard.exception.PostNotDeletableException
import com.kt.kotlinboard.exception.PostNotFoundException
import com.kt.kotlinboard.repository.PostRepository
import com.kt.kotlinboard.service.dto.request.PostCreateRequestDto
import com.kt.kotlinboard.service.dto.request.PostSearchRequestDto
import com.kt.kotlinboard.service.dto.request.PostUpdateRequestDto
import com.kt.kotlinboard.service.dto.request.toEntity
import com.kt.kotlinboard.service.dto.response.PostSummaryResponseDto
import com.kt.kotlinboard.service.dto.response.toSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createPost(requestDto: PostCreateRequestDto): Long {
        return postRepository.save(requestDto.toEntity()).id
    }

    @Transactional
    fun updatePost(id: Long, requestDto: PostUpdateRequestDto): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        post.update(requestDto)
        return id
    }

    @Transactional
    fun deletePost(id: Long, deletedBy: String): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        if (post.createdBy != deletedBy) throw PostNotDeletableException()
        postRepository.delete(post)
        return id
    }

    fun getPost(id: Long): PostDetailResponseDto {
        return postRepository.findByIdOrNull(id)?.toDetailResponseDto() ?: throw PostNotFoundException()
    }

    fun findPageBy(pageRequest: Pageable, postSearchRequestDto: PostSearchRequestDto): Page<PostSummaryResponseDto> {
        return postRepository.findPageBy(pageRequest, postSearchRequestDto).toSummaryResponseDto()
    }
}
