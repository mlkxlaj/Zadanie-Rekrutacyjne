package org.example.dto;

import org.example.Type;

public record PersonDTO(Type type, String personId, String firstName, String lastName, String mobile, String email,
                        String pesel) {
}
