package ru.yandex.praktikum.dto;

import lombok.Data;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
public class CreateUserRequest {

    private String email;
    private String password;
    private String name;
}
