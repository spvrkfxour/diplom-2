package ru.yandex.praktikum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class CreateOrderRequest {

    private List<String> ingredients;
}
