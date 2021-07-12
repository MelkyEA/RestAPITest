package com.mts.test.model;


import org.springframework.data.annotation.Id;

import java.util.Objects;


public class Person {
    @Id
    private long person_id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String typeDocument;
    private String seriesNumber;
    private String birthdate;

    public Person(long person_id, String lastName, String firstName, String middleName,
                  String typeDocument, String seriesNumber, String birthdate) {
        this.person_id = person_id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.typeDocument = typeDocument;
        this.seriesNumber = seriesNumber;
        this.birthdate = birthdate;
    }

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return person_id == person.person_id && lastName.equals(person.lastName) && firstName.equals(person.firstName) && middleName.equals(person.middleName) && typeDocument.equals(person.typeDocument) && seriesNumber.equals(person.seriesNumber) && birthdate.equals(person.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person_id, lastName, firstName, middleName, typeDocument, seriesNumber, birthdate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + person_id +
                ", Фамилия = " + lastName +
                ", Имя = " + firstName +
                ", Отчество = " + middleName +
                ", Тип документа = " + typeDocument +
                ", Серия Номер = " + seriesNumber +
                ", Дата рождения = " + birthdate +
                '}';
    }
}
