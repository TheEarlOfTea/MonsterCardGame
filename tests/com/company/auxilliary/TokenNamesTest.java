package com.company.auxilliary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenNamesTest {

    @Test
    void getUserToken() {
        String token="testUser-mtcgToken";
        assertEquals(0, token.compareTo(TokenNames.getUserToken("testUser")));
    }
}