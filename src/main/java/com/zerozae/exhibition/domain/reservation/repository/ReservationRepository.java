package com.zerozae.exhibition.domain.reservation.repository;

import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import com.zerozae.exhibition.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.member m " +
            "JOIN FETCH r.exhibition e " +
            "WHERE (:email IS NULL OR m.email = :email) " +
            "AND (:name IS NULL OR e.exhibitionName = :exhibitionName)")
    List<Reservation> findReservationByCondition(@Param("email") String email, @Param("exhibitionName") String exhibitionName);
}
