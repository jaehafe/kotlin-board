package com.kt.kotlinboard.controller.dto.post.response

data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: String,
)
