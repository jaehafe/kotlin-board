package com.kt.kotlinboard.repository

import org.springframework.data.jpa.repository.JpaRepository
import javax.xml.stream.events.Comment

interface CommentRepository : JpaRepository<Comment, Long>
