package com.kt.kotlinboard.controller

import com.kt.kotlinboard.controller.dto.post.request.PostCreateRequest
import com.kt.kotlinboard.controller.dto.post.request.PostSearchRequest
import com.kt.kotlinboard.controller.dto.post.request.PostUpdateRequest
import com.kt.kotlinboard.controller.dto.post.request.toDto
import com.kt.kotlinboard.controller.dto.post.response.PostDetailResponse
import com.kt.kotlinboard.controller.dto.post.response.PostSummaryResponse
import com.kt.kotlinboard.controller.dto.post.response.toResponse
import com.kt.kotlinboard.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService,
) {

    @PostMapping("/posts")
    fun createPost(
        @RequestBody postCreateRequest: PostCreateRequest,
    ): Long {
        println("postCreateRequest: ${postCreateRequest.tags}")
        return postService.createPost(postCreateRequest.toDto())
    }

    @PutMapping("/posts/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody postUpdateRequest: PostUpdateRequest,
    ): Long {
        println("tags: ${postUpdateRequest.tags}")
        return postService.updatePost(id, postUpdateRequest.toDto())
    }

    @DeleteMapping("/posts/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam createdBy: String,
    ): Long {
        return postService.deletePost(id, createdBy)
    }

    @GetMapping("/posts/{id}")
    fun getPost(
        @PathVariable id: Long,
    ): PostDetailResponse {
        return postService.getPost(id).toResponse()
    }

    @GetMapping("/posts")
    fun getPosts(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> {
        println("tags: ${postSearchRequest.tag}")
        return postService.findPageBy(pageable, postSearchRequest.toDto()).toResponse()
    }
}
