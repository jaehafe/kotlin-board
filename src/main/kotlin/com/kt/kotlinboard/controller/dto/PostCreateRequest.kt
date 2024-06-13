package com.kt.kotlinboard.controller.dto

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
) {
}
