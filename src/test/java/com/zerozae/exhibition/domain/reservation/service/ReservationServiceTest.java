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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.zerozae.exhibition.factory.reservation.ReservationFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ExhibitionRepository exhibitionRepository;

    private Reservation reservation = createReservation();
    private Exhibition exhibition = reservation.getExhibition();
    private Member member = reservation.getMember();

    @Test
    void 예약_생성_테스트() {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                exhibition.getStartTime(),
                member.getEmail(),
                exhibition.getExhibitionName()
        );

        given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
        given(exhibitionRepository.findByExhibitionName(exhibition.getExhibitionName())).willReturn(Optional.of(exhibition));
        given(exhibitionRepository.isExistBetweenStartAndEndTime(exhibition.getId(), exhibition.getStartTime())).willReturn(true);
        given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);

        // When
        ReservationResponse reservationResponse = reservationService.createReservation(request);

        // Then
        assertThat(reservationResponse.getId()).isEqualTo(reservation.getId());
        verify(memberRepository).findByEmail(member.getEmail());
        verify(exhibitionRepository).findByExhibitionName(exhibition.getExhibitionName());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_회원으로_인한_예약_생성_실패_테스트() {
        // Given
        String notExistEmail = "notExistEmail@gmail.com";
        ReservationCreateRequest request = new ReservationCreateRequest(
                exhibition.getStartTime(),
                notExistEmail,
                exhibition.getExhibitionName()
        );
        given(memberRepository.findByEmail(notExistEmail)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 존재하지_않는_전시회로_인한_예약_생성_실패_테스트() {
        // Given
        String notExistExhibition = "empty";
        ReservationCreateRequest request = new ReservationCreateRequest(
                exhibition.getStartTime(),
                member.getEmail(),
                notExistExhibition
        );
        given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
        given(exhibitionRepository.findByExhibitionName(notExistExhibition)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ExhibitionNotFoundException.class);
    }

    @Test
    void 예약불가_시간설정으로_인한_예약_생성_실패_테스트() {
        // Given
        ReservationCreateRequest request = new ReservationCreateRequest(
                LocalDateTime.now(),
                member.getEmail(),
                exhibition.getExhibitionName()
        );
        given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
        given(exhibitionRepository.findByExhibitionName(exhibition.getExhibitionName())).willReturn(Optional.of(exhibition));

        // When & Then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ImpossibleReservationException.class);
    }

    @Test
    void 전체_예약내역_조회_테스트() {
        // Given
        given(reservationRepository.findAll()).willReturn(List.of());
        
        // When
        reservationService.findAllReservations();
        
        // Then
        verify(reservationRepository).findAll();
    }
    
    @Test
    void 예약_아이디로_조회_테스트() {
        // Given
        given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));
        
        // When
        ReservationResponse reservationResponse = reservationService.findReservationById(reservation.getId());
        
        // Then
        assertThat(reservationResponse.getId()).isEqualTo(reservation.getId());
        verify(reservationRepository).findById(reservation.getId());
    }

    @Test
    void 예약자_이메일로_예약_조회_테스트() {
        // Given
        ReservationCondition condition = new ReservationCondition(member.getEmail(), null);
        given(reservationRepository.findReservationByCondition(condition.email(), condition.exhibitionName())).willReturn(List.of(reservation));

        // When
        List<ReservationResponse> reservations = reservationService.findReservationByCondition(condition);

        // Then
        assertThat(reservations).isNotNull();
        assertThat(reservations.get(0).getNickname()).isEqualTo(member.getNickname());
        verify(reservationRepository).findReservationByCondition(condition.email(), condition.exhibitionName());
    }

    @Test
    void 전시회_이름으로_예약_조회_테스트() {
        // Given
        ReservationCondition condition = new ReservationCondition(null, exhibition.getExhibitionName());
        given(reservationRepository.findReservationByCondition(condition.email(), condition.exhibitionName())).willReturn(List.of(reservation));

        // When
        List<ReservationResponse> reservations = reservationService.findReservationByCondition(condition);

        // Then
        assertThat(reservations).isNotNull();
        assertThat(reservations.get(0).getExhibitionName()).isEqualTo(exhibition.getExhibitionName());
        verify(reservationRepository).findReservationByCondition(condition.email(), condition.exhibitionName());
    }

    @Test
    void 예약_수정_테스트() {
        // Given
        Exhibition newExhibition = new Exhibition(
                "전시 2번",
                "전시 2번 입니다.",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "전시장 1번",
                20000L
        );
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                newExhibition.getStartTime(),
                newExhibition.getExhibitionName());

        given(exhibitionRepository.findByExhibitionName(newExhibition.getExhibitionName())).willReturn(Optional.of(newExhibition));
        given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));

        // When
        reservationService.updateReservation(reservation.getId(), request);

        // Then
        assertThat(reservation.getReservationTime()).isEqualTo(newExhibition.getStartTime());
        assertThat(reservation.getExhibition().getExhibitionName()).isEqualTo(newExhibition.getExhibitionName());
        verify(exhibitionRepository).findByExhibitionName(newExhibition.getExhibitionName());
    }

    @Test
    void 예약_아이디로_삭제_테스트() {
        // Given
        given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));

        // When
        reservationService.deleteReservationById(reservation.getId());

        // Then
        verify(reservationRepository).findById(reservation.getId());
        verify(reservationRepository).deleteById(reservation.getId());
    }

    @Test
    void 예약_아이디로_삭제_실패_테스트() {
        // Given
        Long notExistId = 0L;
        given(reservationRepository.findById(notExistId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteReservationById(notExistId))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 예약_전체_삭제_테스트() {
        // Given && When
        reservationService.deleteAllReservations();

        // Then
        verify(reservationRepository).deleteAll();
    }
}
