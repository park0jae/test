package com.zerozae.exhibition.domain.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozae.exhibition.domain.reservation.dto.ReservationCondition;
import com.zerozae.exhibition.domain.reservation.dto.ReservationCreateRequest;
import com.zerozae.exhibition.domain.reservation.dto.ReservationResponse;
import com.zerozae.exhibition.domain.reservation.dto.ReservationUpdateRequest;
import com.zerozae.exhibition.domain.reservation.entity.Reservation;
import com.zerozae.exhibition.domain.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.zerozae.exhibition.factory.reservation.ReservationFactory.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService service;

    private Reservation reservation = createReservation();

    @Test
    void 예약_생성_테스트() throws Exception {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                reservation.getReservationTime(),
                reservation.getMember().getEmail(),
                reservation.getExhibition().getExhibitionName()
        );

        ReservationResponse response = ReservationResponse.toDto(reservation);
        given(service.createReservation(request)).willReturn(response);

        // When
        mvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Then
        verify(service).createReservation(request);
    }

    @Test
    void 예약_전체_조회_테스트() throws Exception {
        // Given
        List<ReservationResponse> reservations = List.of(ReservationResponse.toDto(reservation));
        given(service.findAllReservations()).willReturn(reservations);

        // When
        mvc.perform(get("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationTime").value(reservation.getReservationTime().toString()))
                .andExpect(jsonPath("$[0].nickname").value(reservation.getMember().getNickname()))
                .andExpect(jsonPath("$[0].exhibitionName").value(reservation.getExhibition().getExhibitionName()));

        // Then
        verify(service).findAllReservations();
    }

    @Test
    void 예약_검색_조건_이메일로_검색_테스트() throws Exception {
        // Given
        ReservationCondition condition = new ReservationCondition(
                reservation.getMember().getEmail(),
                null
        );
        given(service.findReservationByCondition(condition)).willReturn(List.of(ReservationResponse.toDto(reservation)));

        // When
        mvc.perform(get("/api/v1/reservations/condition")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", reservation.getMember().getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationTime").value(reservation.getReservationTime().toString()))
                .andExpect(jsonPath("$[0].nickname").value(reservation.getMember().getNickname()))
                .andExpect(jsonPath("$[0].exhibitionName").value(reservation.getExhibition().getExhibitionName()));

    }

    @Test
    void 예약_검색_조건_전시회명_검색_테스트() throws Exception {
        // Given
        ReservationCondition condition = new ReservationCondition(
                null,
                reservation.getExhibition().getExhibitionName()
        );
        given(service.findReservationByCondition(condition)).willReturn(List.of(ReservationResponse.toDto(reservation)));

        // When
        mvc.perform(get("/api/v1/reservations/condition")
                .contentType(MediaType.APPLICATION_JSON)
                .param("exhibitionName", reservation.getExhibition().getExhibitionName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationTime").value(reservation.getReservationTime().toString()))
                .andExpect(jsonPath("$[0].nickname").value(reservation.getMember().getNickname()))
                .andExpect(jsonPath("$[0].exhibitionName").value(reservation.getExhibition().getExhibitionName()));

    }

    @Test
    void 예약_수정_테스트() throws Exception {
        // Given
        Long reservationId = 1L;
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                LocalDateTime.now(),
                reservation.getExhibition().getExhibitionName()
        );
        given(service.findReservationById(reservationId)).willReturn(ReservationResponse.toDto(reservation));

        // When
        mvc.perform(patch("/api/v1/reservations/{id}", reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        // Then
        verify(service).updateReservation(reservationId, request);
    }

    @Test
    void 예약_아이디로_삭제_테스트() throws Exception {
        // Given
        Long reservationId = 1L;
        given(service.findReservationById(reservationId)).willReturn(ReservationResponse.toDto(reservation));

        // When
        mvc.perform(delete("/api/v1/reservations/{id}", reservationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(service).deleteReservationById(reservationId);
    }

    @Test
    void 예약_전체_삭제_테스트() throws Exception {
        // Given & When
        mvc.perform(delete("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(service).deleteAllReservations();
    }
}
