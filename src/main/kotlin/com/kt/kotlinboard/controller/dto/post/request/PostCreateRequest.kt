package com.kt.kotlinboard.controller.dto.post.request

import com.kt.kotlinboard.service.dto.request.PostCreateRequestDto

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequest.toDto() = PostCreateRequestDto(
    title = title,
    content = content,
    createdBy = createdBy
)
