package ru.itis.karimov.entity;

import lombok.*;
import ru.itis.karimov.entity.enums.Gender;
import ru.itis.karimov.entity.enums.Role;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserEntity {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String hashPassword;
    private Gender gender;
    private String contacts;
    private Role role;
    private Integer course;
    private Integer birthYear;
    private Integer dormitoryId;
    private LocalDateTime createdAt;
    private boolean isBlocked;
    private boolean isConfirmed;

}
