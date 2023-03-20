package com.example.session.repository;

import com.example.session.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT MAX(id) FROM Board")
    Long findFirstId();

    List<Board> findByUsername(String name);
}
