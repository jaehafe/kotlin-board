package com.kt.kotlinboard.service.dto.post.request

data class PostSearchRequestDto(
    val title: String? = null,
    val createdBy: String? = null,
    val tag: String? = null,
)
