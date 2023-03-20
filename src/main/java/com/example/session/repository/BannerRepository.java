package com.example.session.repository;

import com.example.session.model.Banner;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface BannerRepository extends JpaRepository<Banner, Long> {

    Optional<Banner> findById(Long id);

    @Modifying
    @Query(value = "TRUNCATE TABLE banner", nativeQuery = true)
    void truncateTable();

}
