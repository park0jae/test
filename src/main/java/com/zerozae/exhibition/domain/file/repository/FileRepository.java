package com.zerozae.exhibition.domain.file.repository;

import com.zerozae.exhibition.domain.file.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Image, Long> {
}
