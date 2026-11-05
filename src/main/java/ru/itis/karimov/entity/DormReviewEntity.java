package ru.itis.karimov.entity;

import lombok.*;

import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DormReviewEntity {

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private Long id;
    private Long userId;
    private Integer dormitoryId;
    private String text;
    private Integer rating;
    private LocalDateTime createdAt;

    public String getFormattedCreatedAt() {
        return createdAt == null ? "" : createdAt.format(DISPLAY_DATE_FORMAT);
    }

}
