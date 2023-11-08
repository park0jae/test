package com.zerozae.exhibition.domain.reservation.controller;

import com.zerozae.exhibition.domain.reservation.dto.ReservationCondition;
import com.zerozae.exhibition.domain.reservation.dto.ReservationCreateRequest;
import com.zerozae.exhibition.domain.reservation.dto.ReservationResponse;
import com.zerozae.exhibition.domain.reservation.dto.ReservationUpdateRequest;
import com.zerozae.exhibition.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class
ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationCreateRequest reservationCreateRequest) {
        ReservationResponse reservation = reservationService.createReservation(reservationCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/reservations/" + reservation.getId())).body(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAllReservations() {
        List<ReservationResponse> reservations = reservationService.findAllReservations();
        return ResponseEntity.status(OK).body(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> findReservationById(@PathVariable Long id) {
        ReservationResponse reservation = reservationService.findReservationById(id);
        return ResponseEntity.status(OK).body(reservation);
    }

    @GetMapping("/condition")
    public ResponseEntity<List<ReservationResponse>> findReservationByCondition(@Valid @ModelAttribute ReservationCondition reservationCondition) {
        List<ReservationResponse> reservations = reservationService.findReservationByCondition(reservationCondition);
        return ResponseEntity.status(OK).body(reservations);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateReservation(@PathVariable Long id,
                                                  @Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest) {
        reservationService.updateReservation(id, reservationUpdateRequest);
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservationById(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllReservations() {
        reservationService.deleteAllReservations();
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }
}
