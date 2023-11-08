package com.zerozae.exhibition.domain.exhibition.dto;

import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ExhibitionUpdateRequest(

        @Length(min = 2, max = 30, message = "전시회명은 최소 2자, 최대 30자로 설정할 수 있습니다.")
        @Nullable
        String name,

        @Nullable
        String description,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Nullable
        LocalDateTime startTime,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Nullable
        LocalDateTime endTime,

        @Length(min = 2, max = 15, message = "전시회장은 최소 2자, 최대 15자로 설정할 수 있습니다.")
        @Nullable
        String location,

        @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
        @Nullable
        Long price,

        MultipartFile image
) {
}
