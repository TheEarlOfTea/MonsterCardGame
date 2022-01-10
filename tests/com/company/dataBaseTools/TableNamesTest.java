package com.company.dataBaseTools;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableNamesTest {

    String testuser="testUser";

    @Test
    void getUserStackTableName() {
        String expectedTableName= "stack_testUser";
        assertEquals(0, expectedTableName.compareTo(TableNames.getUserStackTableName(testuser)));
    }

    @Test
    void getPackageTableName() {
        String expectedTableName= "package_testUser";
        assertEquals(0, expectedTableName.compareTo(TableNames.getPackageTableName(testuser)));
    }

    @Test
    void getDeckTableName() {
        String expectedTableName= "deck_testUser";
        assertEquals(0, expectedTableName.compareTo(TableNames.getDeckTableName(testuser)));
    }
}