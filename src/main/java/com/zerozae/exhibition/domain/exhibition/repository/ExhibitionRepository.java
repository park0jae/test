package com.zerozae.exhibition.domain.exhibition.repository;

import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    Optional<Exhibition> findByExhibitionName(String exhibitionName);

    @Query("SELECT e FROM Exhibition e " +
            "WHERE (:exhibitionName IS NULL OR e.exhibitionName LIKE %:exhibitionName%) " +
            "AND (:exhibitionTime IS NULL OR :exhibitionTime BETWEEN e.startTime AND e.endTime)")
    List<Exhibition> findAllByCondition(@Param("exhibitionName") String exhibitionName,
                                        @Param("exhibitionTime") LocalDateTime exhibitionTime);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Exhibition e " +
            "WHERE e.id = :exhibitionId " +
            "AND :reservationTime BETWEEN e.startTime AND e.endTime")
    boolean isExistBetweenStartAndEndTime(@Param("exhibitionId") Long exhibitionId, @Param("reservationTime") LocalDateTime reservationTime);

    boolean existsByExhibitionName(String exhibitionName);
}
