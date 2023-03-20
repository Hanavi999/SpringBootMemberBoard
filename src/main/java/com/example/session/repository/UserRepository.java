package com.example.session.repository;

import com.example.session.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

}
