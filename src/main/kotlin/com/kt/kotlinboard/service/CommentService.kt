package com.kt.kotlinboard.service

import com.kt.kotlinboard.exception.CommentNotDeletableException
import com.kt.kotlinboard.exception.CommentNotFoundException
import com.kt.kotlinboard.exception.CommentNotUpdatableException
import com.kt.kotlinboard.exception.PostNotFoundException
import com.kt.kotlinboard.repository.CommentRepository
import com.kt.kotlinboard.repository.PostRepository
import com.kt.kotlinboard.service.dto.comment.request.CommentCreateRequestDto
import com.kt.kotlinboard.service.dto.comment.request.CommentUpdateRequestDto
import com.kt.kotlinboard.service.dto.comment.request.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {

    @Transactional
    fun createComment(postId: Long, createRequestDto: CommentCreateRequestDto): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return commentRepository.save(createRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(id: Long, updateRequestDto: CommentUpdateRequestDto): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        comment.update(updateRequestDto)
        return comment.id
    }

    @Transactional
    fun deleteComment(id: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        if(comment.createdBy != deletedBy) {
            throw CommentNotDeletableException()
        }
        commentRepository.delete(comment)
        return comment.id
    }
}
