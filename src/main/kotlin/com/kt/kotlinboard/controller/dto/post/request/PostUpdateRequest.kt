package com.kt.kotlinboard.controller.dto.post.request

import com.kt.kotlinboard.service.dto.request.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)

fun PostUpdateRequest.toDto() = PostUpdateRequestDto(
    title = title,
    content = content,
    updatedBy = updatedBy
)
