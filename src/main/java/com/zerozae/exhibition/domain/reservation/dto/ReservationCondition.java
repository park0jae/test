package com.zerozae.exhibition.domain.reservation.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;

public record ReservationCondition(

        @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", message = "이메일 형식에 맞게 입력해주세요.")
        @Nullable
        String email,

        @Length(min = 2, max = 30, message = "전시회명은 최소 2자, 최대 30자로 설정할 수 있습니다.")
        @Nullable
        String exhibitionName
) {
}
