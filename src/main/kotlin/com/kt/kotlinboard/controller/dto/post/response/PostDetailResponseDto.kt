package com.kt.kotlinboard.controller.dto.post.response

import com.kt.kotlinboard.domain.Post
import com.kt.kotlinboard.domain.Tag
import com.kt.kotlinboard.service.dto.comment.response.CommentResponseDto
import com.kt.kotlinboard.service.dto.comment.response.toResponseDto

data class PostDetailResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val comments: List<CommentResponseDto>,
    val tags: List<String> = emptyList(),
)

fun Post.toDetailResponseDto() = PostDetailResponseDto(
    id = id,
    title = title,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt.toString(),
    comments = comments.map { it.toResponseDto() },
    tags = tags.map { it.name },
)
