package com.kt.kotlinboard.controller.dto.comment.request

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)
