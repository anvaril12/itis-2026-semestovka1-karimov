package ru.itis.karimov.validation;

import ru.itis.karimov.dto.RoomForm;

import java.util.HashMap;
import java.util.Map;

public class RoomValidator {

    public Map<String, String> validate(RoomForm roomForm) {
        Map<String, String> errors = new HashMap<>();

        String roomNumber = roomForm.roomNumber();
        if(isBlank(roomNumber)) {
            errors.put("roomNumber", "Номер комнаты не должен быть пустым");
        } else if(roomNumber.length() > 20) {
            errors.put("roomNumber", "Номер комнаты не должен быть больше 20 символов");
        }

        String capacity = roomForm.capacity();
        if(isBlank(capacity)) {
            errors.put("capacity", "Вместительность комнаты не должна быть пустой");
        } else {
            try {
                int c = Integer.parseInt(capacity);
                if(c > 4 || c < 2) {
                    errors.put("capacity", "Вместительность комнаты должна быть от 2 до 4");
                }
            } catch (NumberFormatException e) {
                errors.put("capacity", "Введите число");
            }
        }


        String description = roomForm.description();
        if(!isBlank(description) && description.length() > 255) {
            errors.put("description", "Не больше 255 символов");
        }
        return errors;
    }
    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
