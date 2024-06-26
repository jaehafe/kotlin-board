package com.kt.kotlinboard.service

import com.kt.kotlinboard.controller.dto.post.response.PostDetailResponseDto
import com.kt.kotlinboard.controller.dto.post.response.toDetailResponseDto
import com.kt.kotlinboard.exception.PostNotDeletableException
import com.kt.kotlinboard.exception.PostNotFoundException
import com.kt.kotlinboard.repository.PostRepository
import com.kt.kotlinboard.repository.TagRepository
import com.kt.kotlinboard.service.dto.post.request.PostCreateRequestDto
import com.kt.kotlinboard.service.dto.post.request.PostSearchRequestDto
import com.kt.kotlinboard.service.dto.post.request.PostUpdateRequestDto
import com.kt.kotlinboard.service.dto.post.request.toEntity
import com.kt.kotlinboard.service.dto.post.response.PostSummaryResponseDto
import com.kt.kotlinboard.service.dto.post.response.toSummaryResponseDto
import com.kt.kotlinboard.service.dto.tag.request.toSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val likeService: LikeService,
    private val tagRepository: TagRepository,
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
        val likeCount = likeService.countLike(id)
        return postRepository.findByIdOrNull(id)?.toDetailResponseDto(likeCount) ?: throw PostNotFoundException()
    }

    fun findPageBy(pageRequest: Pageable, postSearchRequestDto: PostSearchRequestDto): Page<PostSummaryResponseDto> {
        postSearchRequestDto.tag?.let {
            return tagRepository.findPageBy(pageRequest, it).toSummaryResponseDto(likeService::countLike)
        }
        return postRepository.findPageBy(pageRequest, postSearchRequestDto)
            .toSummaryResponseDto(likeService::countLike)
    }
}
