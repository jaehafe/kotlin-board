package com.kt.kotlinboard.controller.dto.comment.response

data class CommentResponse(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)
