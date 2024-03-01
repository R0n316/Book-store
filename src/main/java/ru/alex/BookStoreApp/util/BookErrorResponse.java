package ru.alex.BookStoreApp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BookErrorResponse {
    private String message;
    private long timestamp;
}
