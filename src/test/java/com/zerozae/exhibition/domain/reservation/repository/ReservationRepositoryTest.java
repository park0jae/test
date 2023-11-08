package com.zerozae.exhibition.domain.reservation.repository;


import com.zerozae.exhibition.domain.reservation.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.zerozae.exhibition.factory.reservation.ReservationFactory.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository repository;
    private Reservation reservation = createReservation();

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void 예약_저장_테스트() {
        // Given

        // When
        Reservation savedReservation = repository.save(reservation);

        // Then
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getId()).isEqualTo(reservation.getId());
        assertThat(savedReservation.getMember().getNickname()).isEqualTo(reservation.getMember().getNickname());
        assertThat(savedReservation.getExhibition().getExhibitionName()).isEqualTo(reservation.getExhibition().getExhibitionName());
        assertThat(savedReservation.getReservationTime()).isEqualTo(reservation.getReservationTime());
    }

    @Test
    void 예약_아이디로_조회_테스트() {
        // Given
        repository.save(reservation);

        // When
        Optional<Reservation> findReservation = repository.findById(reservation.getId());

        // Then
        assertThat(findReservation).isPresent();
        assertThat(findReservation.get().getExhibition().getExhibitionName()).isEqualTo(reservation.getExhibition().getExhibitionName());
        assertThat(findReservation.get().getMember().getNickname()).isEqualTo(reservation.getMember().getNickname());
    }
}
