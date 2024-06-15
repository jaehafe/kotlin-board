package com.kt.kotlinboard.controller.dto.comment.request

import com.kt.kotlinboard.service.dto.comment.request.CommentUpdateRequestDto

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

fun CommentUpdateRequest.toDto() = CommentUpdateRequestDto(
    content = content,
    updatedBy = updatedBy
)
