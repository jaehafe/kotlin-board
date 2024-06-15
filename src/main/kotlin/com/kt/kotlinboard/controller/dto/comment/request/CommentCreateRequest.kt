package com.kt.kotlinboard.controller.dto.comment.request

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)
