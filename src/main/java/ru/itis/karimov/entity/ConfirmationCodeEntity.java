package ru.itis.karimov.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmationCodeEntity {

    private Long id;
    private Long userId;
    private String code;
    private LocalDateTime sendAt;

}
