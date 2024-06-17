package com.kt.kotlinboard.repository

import com.kt.kotlinboard.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long> {

    fun countByPostId(postId: Long): Long
}
