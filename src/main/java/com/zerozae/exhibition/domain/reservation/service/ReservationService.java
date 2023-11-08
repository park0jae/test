package com.zerozae.exhibition.domain.reservation.service;

import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import com.zerozae.exhibition.domain.exhibition.exception.ExhibitionNotFoundException;
import com.zerozae.exhibition.domain.exhibition.repository.ExhibitionRepository;
import com.zerozae.exhibition.domain.member.entity.Member;
import com.zerozae.exhibition.domain.member.exception.MemberNotFoundException;
import com.zerozae.exhibition.domain.member.repository.MemberRepository;
import com.zerozae.exhibition.domain.reservation.dto.ReservationCondition;
import com.zerozae.exhibition.domain.reservation.dto.ReservationCreateRequest;
import com.zerozae.exhibition.domain.reservation.dto.ReservationResponse;
import com.zerozae.exhibition.domain.reservation.dto.ReservationUpdateRequest;
import com.zerozae.exhibition.domain.reservation.entity.Reservation;
import com.zerozae.exhibition.domain.reservation.exception.ImpossibleReservationException;
import com.zerozae.exhibition.domain.reservation.exception.ReservationNotFoundException;
import com.zerozae.exhibition.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ExhibitionRepository exhibitionRepository;

    public ReservationResponse createReservation(ReservationCreateRequest reservationCreateRequest) {
        Member member = memberRepository.findByEmail(reservationCreateRequest.email()).orElseThrow(MemberNotFoundException::new);
        Exhibition exhibition = exhibitionRepository.findByExhibitionName(reservationCreateRequest.exhibitionName()).orElseThrow(ExhibitionNotFoundException::new);

        if(!exhibitionRepository.isExistBetweenStartAndEndTime(exhibition.getId(), reservationCreateRequest.reservationTime())){
            throw new ImpossibleReservationException();
        }
        Reservation savedReservation = reservationRepository.save(new Reservation(reservationCreateRequest.reservationTime(), member, exhibition));
        return ReservationResponse.toDto(savedReservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(ReservationResponse::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ReservationResponse findReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
        return ReservationResponse.toDto(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findReservationByCondition(ReservationCondition reservationCondition) {
        List<Reservation> reservations = reservationRepository.findReservationByCondition(
                reservationCondition.email(),
                reservationCondition.exhibitionName());

        return reservations.stream().map(ReservationResponse::toDto).toList();
    }

    public void updateReservation(Long id, ReservationUpdateRequest reservationUpdateRequest) {
        Exhibition exhibition = exhibitionRepository.findByExhibitionName(reservationUpdateRequest.exhibitionName()).orElseThrow(ExhibitionNotFoundException::new);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
        reservation.updateReservation(reservationUpdateRequest.reservationTime(), exhibition);
    }

    public void deleteReservationById(Long id) {
        reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
        reservationRepository.deleteById(id);
    }

    public void deleteAllReservations() {
        reservationRepository.deleteAll();
    }
}
