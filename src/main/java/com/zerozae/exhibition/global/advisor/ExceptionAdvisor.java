package com.zerozae.exhibition.global.advisor;

import com.zerozae.exhibition.domain.exhibition.exception.DuplicateExhibitionException;
import com.zerozae.exhibition.domain.exhibition.exception.ExhibitionNotFoundException;
import com.zerozae.exhibition.domain.file.exception.NoExtException;
import com.zerozae.exhibition.domain.file.exception.UnSupportExtException;
import com.zerozae.exhibition.domain.member.exception.DuplicateMemberException;
import com.zerozae.exhibition.domain.member.exception.MemberNotFoundException;
import com.zerozae.exhibition.domain.reservation.exception.ImpossibleReservationException;
import com.zerozae.exhibition.domain.reservation.exception.ReservationNotFoundException;
import com.zerozae.exhibition.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorResponse> exceptionMessage(Exception e){
        log.error("Error Message ={}", e.getMessage());
        HttpStatus status = INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getMessage(), status.name());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        HttpStatus status = BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getMessage(), status.name());
        log.warn("Error Message = {}", errorMessage);
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ResponseEntity<ErrorResponse> memberNotFoundException(MemberNotFoundException e) {
        HttpStatus status = NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name(), "회원을 찾을 수 없습니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ResponseEntity<ErrorResponse> reservationNotFoundException(ReservationNotFoundException e) {
        HttpStatus status = NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name() , "예약을 찾을 수 없습니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(ExhibitionNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ResponseEntity<ErrorResponse> exhibitionNotFoundException(ExhibitionNotFoundException e) {
        HttpStatus status = NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name(), "전시회를 찾을 수 없습니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    @ResponseStatus(CONFLICT)
    protected ResponseEntity<ErrorResponse> duplicateMemberException(DuplicateMemberException e) {
        HttpStatus status = CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name(), "이미 존재하는 회원입니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(DuplicateExhibitionException.class)
    @ResponseStatus(CONFLICT)
    protected ResponseEntity<ErrorResponse> duplicateExhibitionException(DuplicateExhibitionException e) {
        HttpStatus status = CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name(), "이미 존재하는 전시회입니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(ImpossibleReservationException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<ErrorResponse> ImpossibleReservationException(ImpossibleReservationException e) {
        HttpStatus status = BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name(), "예약이 불가능한 시간대입니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(UnSupportExtException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    protected ResponseEntity<ErrorResponse> unSupportExtException(UnSupportExtException e) {
        HttpStatus status = UNSUPPORTED_MEDIA_TYPE;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name(), "지원하지 않는 미디어 타입입니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }

    @ExceptionHandler(NoExtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponse> noExtException(NoExtException e) {
        HttpStatus status = BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), status.name(), "업로드한 파일의 확장자를 찾을 수 없습니다.");
        log.warn("Error Message = {}", errorResponse.message());
        return ErrorResponse.toResponseEntity(errorResponse);
    }
}
