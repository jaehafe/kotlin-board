package com.kt.kotlinboard.repository

import com.kt.kotlinboard.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>
