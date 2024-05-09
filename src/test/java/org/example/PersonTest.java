package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void testIsFirstNameValid() {
        Person person = new Person();
        assertTrue(person.isFirstNameValid("John"));
        assertTrue(person.isFirstNameValid("Mikolaj"));
        assertFalse(person.isFirstNameValid(""));
        assertFalse(person.isFirstNameValid(null));
    }

    @Test
    void testIsLastNameValid() {
        Person person = new Person();
        assertTrue(person.isLastNameValid("Doe"));
        assertTrue(person.isLastNameValid("Kowaszewicz"));
        assertFalse(person.isLastNameValid(""));
        assertFalse(person.isLastNameValid(null));
    }

    @Test
    void testIsMobileValid() {
        Person person = new Person();
        assertTrue(person.isMobileValid("+48 123 456 789"));
        assertTrue(person.isMobileValid("+48 881 604 800"));
        assertTrue(person.isMobileValid("123-456-789"));
        assertTrue(person.isMobileValid("123.456.789"));
        assertTrue(person.isMobileValid("123 456 789"));
        assertTrue(person.isMobileValid("123456789"));
        assertFalse(person.isMobileValid(""));
        assertFalse(person.isMobileValid(null));
    }

    @Test
    void testIsPeselValid() {
        Person person = new Person();
        assertTrue(person.isPeselValid("64042999928"));
        assertTrue(person.isPeselValid("02261905898"));
        assertFalse(person.isPeselValid("97031003021"));
        assertFalse(person.isPeselValid("64  0429  999  28"));
        assertFalse(person.isPeselValid("1"));
        assertFalse(person.isPeselValid(""));
        assertFalse(person.isPeselValid(null));
    }

    @Test
    void testIsEmailValid() {
        Person person = new Person();
        assertTrue(person.isEmailValid("mkowaszewicz@gmail.com"));
        assertFalse(person.isEmailValid("mkowaszewiczgmail.com"));
        assertFalse(person.isEmailValid(""));
        assertFalse(person.isEmailValid(null));
    }




}