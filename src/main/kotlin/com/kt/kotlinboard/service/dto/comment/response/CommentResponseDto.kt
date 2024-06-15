package com.kt.kotlinboard.service.dto.comment.response

import com.kt.kotlinboard.domain.Comment

data class CommentResponseDto(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

fun Comment.toResponseDto() = CommentResponseDto(
    id = id,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt.toString()
)
