package com.mts.test.exceptions;

public class CurrencyValidException  extends Exception{

    public CurrencyValidException(String currency) {
        super("Данный код валюты " + currency + " не прошел валидацию");
    }
}
