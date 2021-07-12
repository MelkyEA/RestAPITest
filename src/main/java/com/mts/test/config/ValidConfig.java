package com.mts.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ValidConfig {
    @Value("${valid.types}")
    private final Set<String> types = new HashSet<>();
    @Value("${valid.currency}")
    private final Set<String> currency = new HashSet<>();

    //Проверка на тип документа
    public Boolean validType(String type){
        return types.contains(type);
    }

    //Проверка на код валюты
    public Boolean validCurrency(String accCurrency){
        return currency.contains(accCurrency);
    }
}
