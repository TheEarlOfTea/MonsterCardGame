package com.company.auxilliary.enumUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringToEnumConverterTest {

    @Test
    void getElement() {
        String fire= "FIRE";
        String water= "WATER";
        String plant= "PLANT";
        String normal= "NORMAL";
        assertEquals(0,fire.compareTo(StringToEnumConverter.getElement("fire").toString()));
        assertEquals(0,water.compareTo(StringToEnumConverter.getElement("WATER").toString()));
        assertEquals(0,plant.compareTo(StringToEnumConverter.getElement("pLANT").toString()));
        assertEquals(0,normal.compareTo(StringToEnumConverter.getElement("NORMal").toString()));
    }

    @Test
    void getCardType() {
        String creature= "CREATURE";
        String spell= "SPELL";
        assertEquals(0, creature.compareTo(StringToEnumConverter.getCardType("creature").toString()));
        assertEquals(0, spell.compareTo(StringToEnumConverter.getCardType("spELL").toString()));
    }

}