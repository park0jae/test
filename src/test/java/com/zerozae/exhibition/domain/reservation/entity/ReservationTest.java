package com.zerozae.exhibition.domain.reservation.entity;

import com.zerozae.exhibition.domain.reservation.dto.ReservationUpdateRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.zerozae.exhibition.factory.reservation.ReservationFactory.*;
import static org.assertj.core.api.Assertions.*;

class ReservationTest {

    @Test
    void 예약_생성_테스트() {
        // Given
        Reservation reservation = createReservation();

        // When & Then
        assertThat(reservation).isNotNull();
    }

    @Test
    void 예약_수정_테스트() {
        // Given
        Reservation reservation = createReservation();
        LocalDateTime updateTime = LocalDateTime.now();
        ReservationUpdateRequest request = new ReservationUpdateRequest(updateTime, reservation.getExhibition().getExhibitionName());

        // When
        reservation.updateReservation(request.reservationTime(), reservation.getExhibition());

        // Then
        assertThat(reservation.getReservationTime()).isEqualTo(updateTime);
        assertThat(reservation.getExhibition().getExhibitionName()).isEqualTo(request.exhibitionName());
    }
}
