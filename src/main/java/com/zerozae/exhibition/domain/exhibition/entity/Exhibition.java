package com.zerozae.exhibition.domain.exhibition.entity;

import com.zerozae.exhibition.domain.exhibition.dto.ExhibitionUpdateRequest;
import com.zerozae.exhibition.domain.file.entity.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    @Column(name = "exhibition_name")
    private String exhibitionName;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private long price;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    public Exhibition(String exhibitionName, String description, LocalDateTime startTime, LocalDateTime endTime, String location, long price) {
        this.exhibitionName = exhibitionName;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }


    public Exhibition(String exhibitionName, String description, LocalDateTime startTime, LocalDateTime endTime, String location, long price, Image image) {
        this.exhibitionName = exhibitionName;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.image = image;
    }

    public void updateExhibition(ExhibitionUpdateRequest exhibitionUpdateRequest) {
        if (exhibitionUpdateRequest.name() != null) {
            this.exhibitionName = exhibitionUpdateRequest.name();
        }
        if (exhibitionUpdateRequest.description() != null) {
            this.description = exhibitionUpdateRequest.description();
        }
        if (exhibitionUpdateRequest.startTime() != null) {
            this.startTime = exhibitionUpdateRequest.startTime();
        }
        if (exhibitionUpdateRequest.endTime() != null) {
            this.endTime = exhibitionUpdateRequest.endTime();
        }
        if (exhibitionUpdateRequest.location() != null) {
            this.location = exhibitionUpdateRequest.location();
        }
    }

    public String updateImage(MultipartFile image) {
        if (image != null) {
            this.image = new Image(image.getOriginalFilename());
            return this.image.getUniqueName();
        } else {
            String uniqueNameForDelete = this.image.getUniqueName();
            this.image = null;
            return uniqueNameForDelete;
        }
    }
}
