package com.zerozae.exhibition.factory.exhibition;

import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;

import java.time.LocalDateTime;

public class ExhibitionFactory {

    public static Exhibition createExhibition() {
        return new Exhibition(
                "전시 1",
                "전시 1입니다.",
                LocalDateTime.of(2023, 11, 3, 12, 0),
                LocalDateTime.of(2023, 11, 5, 18, 0),
                "전시장 1번",
                10000);
    }
}
