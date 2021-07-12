package com.mts.test.exceptions;

public class TypeValidException extends Exception{

    public TypeValidException(String type) {
        super("Данный тип документа " + type + " не прошел валидацию");
    }
}
