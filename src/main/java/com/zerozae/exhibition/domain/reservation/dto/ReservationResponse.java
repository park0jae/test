package com.zerozae.exhibition.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerozae.exhibition.domain.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationResponse {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime reservationTime;
    private String nickname;
    private String exhibitionName;

    public static ReservationResponse toDto(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getReservationTime(),
                reservation.getMember().getNickname(),
                reservation.getExhibition().getExhibitionName()
        );
    }
}
