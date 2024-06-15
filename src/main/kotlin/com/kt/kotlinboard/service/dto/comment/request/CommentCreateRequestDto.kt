package com.kt.kotlinboard.service.dto.comment.request

import com.kt.kotlinboard.domain.Comment
import com.kt.kotlinboard.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post) = Comment(
    content = content,
    createdBy = createdBy,
    post = post,
)
