package ru.yandex.praktikum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CreateOrderRequest {

    private String[] ingredients;
}
