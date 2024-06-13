package com.kt.kotlinboard.controller.dto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)
