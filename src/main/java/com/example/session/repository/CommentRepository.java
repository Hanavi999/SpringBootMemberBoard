package com.example.session.repository;

import com.example.session.model.Comment;
import com.example.session.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUsername(String name);

    List<Comment> findByBoardId(Long id);
}
