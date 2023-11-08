package com.zerozae.exhibition.domain.exhibition.service;

import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCondition;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionCreateRequest;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionResponse;
import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionUpdateRequest;
import com.zerozae.exhibition.domain.exhibition.entity.Exhibition;
import com.zerozae.exhibition.domain.exhibition.exception.DuplicateExhibitionException;
import com.zerozae.exhibition.domain.exhibition.exception.ExhibitionNotFoundException;
import com.zerozae.exhibition.domain.exhibition.repository.ExhibitionRepository;
import com.zerozae.exhibition.domain.file.entity.Image;
import com.zerozae.exhibition.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final FileService fileService;

    public ExhibitionResponse createExhibition(ExhibitionCreateRequest exhibitionCreateRequest) {
        validateDuplicateExhibition(exhibitionCreateRequest.exhibitionName());

        MultipartFile uploadImage = exhibitionCreateRequest.image();
        if(uploadImage != null) {
            String originalFilename = uploadImage.getOriginalFilename();
            Image image = new Image(originalFilename);
            fileService.upload(uploadImage, image.getUniqueName());
        }
        Exhibition exhibition = exhibitionRepository.save(exhibitionCreateRequest.toEntity());
        return ExhibitionResponse.toDto(exhibition);
    }

    @Transactional(readOnly = true)
    public List<ExhibitionResponse> findAllExhibitions() {
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        return exhibitions.stream().map(ExhibitionResponse::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ExhibitionResponse findExhibitionById(Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id).orElseThrow(ExhibitionNotFoundException::new);
        return ExhibitionResponse.toDto(exhibition);
    }

    @Transactional(readOnly = true)
    public List<ExhibitionResponse> findExhibitionByCondition(ExhibitionCondition exhibitionCondition) {
        List<Exhibition> exhibitions = exhibitionRepository.findAllByCondition(
                exhibitionCondition.exhibitionName(),
                exhibitionCondition.exhibitionTime());

        return exhibitions.stream().map(ExhibitionResponse::toDto).toList();
    }

    public void updateExhibition(Long id, ExhibitionUpdateRequest exhibitionUpdateRequest) {
        validateDuplicateExhibition(exhibitionUpdateRequest.name());
        Exhibition exhibition = exhibitionRepository.findById(id).orElseThrow(ExhibitionNotFoundException::new);
        exhibition.updateExhibition(exhibitionUpdateRequest);
        if(exhibitionUpdateRequest.image() != null) {
            String exhibitionImage = exhibition.updateImage(exhibitionUpdateRequest.image());
            imageServerUpdate(exhibitionUpdateRequest.image(), exhibition, exhibitionImage);
        }
    }

    public void deleteExhibitionById(Long id) {
        exhibitionRepository.findById(id).orElseThrow(ExhibitionNotFoundException::new);
        exhibitionRepository.deleteById(id);
    }

    public void deleteAllExhibitions() {
        exhibitionRepository.deleteAll();
    }

    private void validateDuplicateExhibition(String exhibitionName) {
        if(exhibitionRepository.existsByExhibitionName(exhibitionName)) {
            throw new DuplicateExhibitionException();
        }
    }

    private void imageServerUpdate(MultipartFile file, Exhibition exhibition, String image) {
        if (file != null) {
            fileService.upload(file, exhibition.getImage().getUniqueName());
        } else {
            fileService.delete(image);
        }
    }
}
