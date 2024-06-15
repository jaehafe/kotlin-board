package com.kt.kotlinboard.service.dto.request

data class PostUpdateRequestDto(
    val title: String,
    val content: String,
    val updatedBy: String,
)
