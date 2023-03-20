package com.example.session.repository;

import com.example.session.model.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<BoardLike, Long> {
    Long countByBoardId(Long boardId);

    Optional<BoardLike> findByBoardIdAndUsername(Long boardId, String username);

}
