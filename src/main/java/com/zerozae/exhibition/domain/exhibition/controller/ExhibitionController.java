package com.zerozae.exhibition.domain.exhibition.controller;

import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCondition;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCreateRequest;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionResponse;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionUpdateRequest;
import com.zerozae.exhibition.domain.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    @PostMapping
    public ResponseEntity<ExhibitionResponse> createExhibition(@Valid @ModelAttribute ExhibitionCreateRequest exhibitionCreateRequest) {
        ExhibitionResponse exhibition = exhibitionService.createExhibition(exhibitionCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/exhibitions/" + exhibition.getId())).body(exhibition);
    }

    @GetMapping
    public ResponseEntity<List<ExhibitionResponse>> findAllExhibitions() {
        List<ExhibitionResponse> exhibitions = exhibitionService.findAllExhibitions();
        return ResponseEntity.status(OK).body(exhibitions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExhibitionResponse> findExhibitionById(@PathVariable Long id) {
        ExhibitionResponse exhibition = exhibitionService.findExhibitionById(id);
        return ResponseEntity.status(OK).body(exhibition);
    }

    @GetMapping("/condition")
    public ResponseEntity<List<ExhibitionResponse>> findExhibitionByCondition(@Valid @ModelAttribute ExhibitionCondition exhibitionCondition) {
        return ResponseEntity.status(OK).body(exhibitionService.findExhibitionByCondition(exhibitionCondition));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateExhibition(@PathVariable Long id, @Valid @ModelAttribute ExhibitionUpdateRequest exhibitionUpdateRequest) {
        exhibitionService.updateExhibition(id, exhibitionUpdateRequest);
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExhibitionById(@PathVariable Long id) {
        exhibitionService.deleteExhibitionById(id);
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllExhibitions() {
        exhibitionService.deleteAllExhibitions();
        return ResponseEntity.status(NO_CONTENT).body("완료 되었습니다.");
    }
}
