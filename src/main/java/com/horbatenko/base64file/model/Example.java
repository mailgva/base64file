package com.horbatenko.base64file.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Getter
@NoArgsConstructor
public class Example {
    private String context = Base64.getEncoder().encodeToString("Hello! This is example ".getBytes());
    private String ext = "txt";
}
