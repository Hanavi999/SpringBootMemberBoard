package com.example.session.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "사용자"),

    ADMIN("ROLE_ADMIN", "관리자"),

    BAN("ROLE_BAN", "밴");

    private final String key;

    private final String value;

}
