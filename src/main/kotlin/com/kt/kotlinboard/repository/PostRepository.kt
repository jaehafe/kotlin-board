package com.kt.kotlinboard.repository

import com.kt.kotlinboard.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>
