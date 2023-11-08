package com.zerozae.exhibition.domain.exhibition.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public record ExhibitionCondition(
        @Length(min = 2, max = 30, message = "전시회명은 최소 2자, 최대 30자로 설정할 수 있습니다.")
        @Nullable
        String exhibitionName,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Nullable
        LocalDateTime exhibitionTime
) {
}
