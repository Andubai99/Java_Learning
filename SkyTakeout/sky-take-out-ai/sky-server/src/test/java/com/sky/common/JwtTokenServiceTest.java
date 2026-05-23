package com.sky.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenServiceTest {

    @Test
    void parsesTokenCreatedForEmployee() {
        JwtTokenService service = new JwtTokenService("01234567890123456789012345678901", 3600);

        String token = service.createToken(new TokenSubject(7L, "employee"));
        TokenSubject subject = service.parse(token);

        assertEquals(7L, subject.id());
        assertEquals("employee", subject.role());
    }

    @Test
    void rejectsTamperedToken() {
        JwtTokenService service = new JwtTokenService("01234567890123456789012345678901", 3600);

        String token = service.createToken(new TokenSubject(9L, "user"));
        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertThrows(BusinessException.class, () -> service.parse(tampered));
    }
}
