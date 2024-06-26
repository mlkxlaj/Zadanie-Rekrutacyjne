package org.example;

import java.util.regex.Pattern;

public class Person {
    private String personId;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String pesel;
    private Type type;

    public Person(Type type, String firstName, String lastName, String mobile, String pesel, String email) {
        if
        (
                type == null ||
                        !isFirstNameValid(firstName) ||
                        !isLastNameValid(lastName) ||
                        !isMobileValid(mobile) ||
                        !isEmailValid(email) ||
                        !isPeselValid(pesel)
        ) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.pesel = pesel;
    }

    //made this constructor for testing
    public Person(Type type, String personId, String firstName, String lastName, String mobile, String pesel, String email) {
        this.type = type;
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.pesel = pesel;
    }

    public Person() {
    }

    public boolean isFirstNameValid(String firstName) {
        return firstName != null && !firstName.trim().isEmpty();
    }

    public boolean isLastNameValid(String lastName) {
        return lastName != null && !lastName.trim().isEmpty();
    }

    public boolean isMobileValid(String mobile) {
        String allCountryRegex = "^(\\+\\d{1,3}( )?)?(\\d{3}[- .]?){2}\\d{3}$";
        Pattern pattern = Pattern.compile(allCountryRegex);
        return mobile != null && !mobile.trim().isEmpty() && pattern.matcher(mobile).matches();
    }

    public boolean isPeselValid(String pesel) {
        if (pesel == null || pesel.trim().isEmpty() || pesel.length() != 11) {
            return false;
        }

        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;

        for (int i = 0; i < weights.length; i++) {
            sum += Integer.parseInt(String.valueOf(pesel.charAt(i))) * weights[i];
        }

        int lastDigit = Integer.parseInt(String.valueOf(pesel.charAt(10)));
        int checkDigit = (10 - (sum % 10)) % 10;

        return checkDigit == lastDigit;
    }

    public boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    public boolean isTypeValid(Type type) {
        return type != null;
    }

    public void setType(Type type) {
        if (!isTypeValid(type)) {
            throw new IllegalArgumentException("Invalid type");
        }
        this.type = type;
    }

    public void setPersonId(String personId) {
        if (personId == null || personId.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid personId");
        }
        this.personId = personId;
    }

    public void setFirstName(String firstName) {
        if (!isFirstNameValid(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (!isLastNameValid(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        this.lastName = lastName;
    }

    public void setMobile(String mobile) {
        if (!isMobileValid(mobile)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }

    public void setPesel(String pesel) {
        if (!isPeselValid(pesel)) {
            throw new IllegalArgumentException("Invalid PESEL");
        }
        this.pesel = pesel;
    }

    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPesel() {
        return pesel;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", pesel='" + pesel + '\'' +
                ", type=" + type +
                '}';
    }
}