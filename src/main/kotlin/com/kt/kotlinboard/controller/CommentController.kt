package com.kt.kotlinboard.controller

import com.kt.kotlinboard.controller.dto.comment.request.CommentCreateRequest
import com.kt.kotlinboard.controller.dto.comment.request.CommentUpdateRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController {

    @PostMapping("posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long {
        println("content: ${commentCreateRequest.content}")
        println("createdBy: ${commentCreateRequest.createdBy}")
        return 1L
    }

    @PutMapping("comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long {
        println("content: ${commentUpdateRequest.content}")
        println("updatedBy: ${commentUpdateRequest.updatedBy}")

        return commentId
    }

    @DeleteMapping("comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long {
        println("deletedBy: $deletedBy")
        return commentId
    }
}
