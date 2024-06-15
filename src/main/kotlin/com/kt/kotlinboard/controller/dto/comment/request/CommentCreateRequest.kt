package com.kt.kotlinboard.controller.dto.comment.request

import com.kt.kotlinboard.service.dto.comment.request.CommentCreateRequestDto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequest.toDto() = CommentCreateRequestDto(
    content = content,
    createdBy = createdBy
)
