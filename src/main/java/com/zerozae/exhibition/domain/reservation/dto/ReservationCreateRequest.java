package com.zerozae.exhibition.domain.reservation.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public record ReservationCreateRequest(

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @NotNull(message = "예약 날짜 및 시간은 필수 입력 항목입니다.")
        LocalDateTime reservationTime,

        @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", message = "이메일 형식에 맞게 입력해주세요.")
        @NotBlank(message = "이메일은 필수 항목입니다.")
        String email,

        @Length(min = 2, max = 30, message = "전시회명은 최소 2자, 최대 30자로 설정할 수 있습니다.")
        @NotBlank(message = "전시회명은 필수 항목입니다.")
        String exhibitionName
) {
}
