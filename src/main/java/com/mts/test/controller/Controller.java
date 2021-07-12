package com.mts.test.controller;


import com.mts.test.config.ValidConfig;
import com.mts.test.exceptions.CurrencyValidException;
import com.mts.test.exceptions.TypeValidException;
import com.mts.test.model.BankAccount;
import com.mts.test.repos.Repository;
import com.mts.test.model.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("person")
public class Controller {


    private ValidConfig validConfig;

    static final Logger log = LoggerFactory.getLogger(Controller.class);

    public Controller(ValidConfig validConfig) {
        this.validConfig = validConfig;
        Repository.createTable();
    }

    @GetMapping
    public List<Map<String, String>> getPersons(){
        List<Person> fromDB = Repository.findAll();
        List<Map<String, String>> result = new ArrayList<>();
        for(Person p : fromDB){
            result.add(new LinkedHashMap<>() {{
                put("Id", String.valueOf(p.getPerson_id()));
                put("First Name", p.getFirstName());
                put("Last Name", p.getLastName());
                put("Middle Name", p.getMiddleName());
                put("Type of document", p.getTypeDocument());
                put("Series-Number of document", p.getSeriesNumber());
                put("Birthdate", p.getBirthdate());
                       }}
            );
        }
        log.info("Call method GET for all persons.");
        return result;
    }

    @PostMapping
    public Map<String, String> newPerson(String lastName, String firstName, String middleName,
                                         String typeDocument, String seriesNumber, String birthdate){
        Map<String, String> result = new LinkedHashMap<>();
        try {
            if(!validConfig.validType(typeDocument)){
                throw new TypeValidException(typeDocument);
            }
            Person p = Repository.createNew(lastName, firstName, middleName, typeDocument, seriesNumber, birthdate);
            result.put("Id", String.valueOf(p.getPerson_id()));
            result.put("First Name", p.getFirstName());
            result.put("Last Name", p.getLastName());
            result.put("Middle Name", p.getMiddleName());
            result.put("Type of document", p.getTypeDocument());
            result.put("Series-Number of document", p.getSeriesNumber());
            result.put("Birthdate", p.getBirthdate());
            log.info("New person add: {}", p);
        }catch (TypeValidException e){
            e.printStackTrace();
            log.error("Exception in valid type document: {}", e);
            result.put("Message", "Ошибка валидации кода документа");
        }

        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deletePerson(@PathVariable String id){
        Repository.deletePerson(Long.parseLong(id));
        Map<String, String> result = new LinkedHashMap<>(){{
            put("Message", "Person id=" + id + " deleted successfully");
        }
        };
        log.info("Delete person with id = {}", id);
        return result;
    }

    @PostMapping("/bank-account")
    public Map<String, String> createBankAccount(String number, String currency, String id){
        Map<String, String> result = new LinkedHashMap<>();
        try {
           if(!validConfig.validCurrency(currency)){
               throw new CurrencyValidException(currency);
           }
            BankAccount bankAccount = Repository.createBankAccount(number, currency, Long.parseLong(id));
            result.put("Account ID", String.valueOf(bankAccount.getAccountId()));
            result.put("Account Number", bankAccount.getNumber());
            result.put("Account Currency", bankAccount.getCurrency());
            result.put("Person ID", String.valueOf(bankAccount.getPersonId()));
            log.info("Create Bank account {}", bankAccount);
        }catch (CurrencyValidException e){
            e.printStackTrace();
            log.error("Exception in valid currency: {}", e);
            result.put("Message", "Ошибка валидации кода валют");
        }
        return result;
    }

    @GetMapping("/bank-account/{id}")
    public List<Map<String, String>> personBankAccounts(@PathVariable String id){
        List<BankAccount> fromDB = Repository.getPersonBankAccounts(Long.parseLong(id));
        List<Map<String, String>> result = new ArrayList<>();
        for(BankAccount bankAccount : fromDB){
            result.add(new LinkedHashMap<>(){{
                put("Account ID", String.valueOf(bankAccount.getAccountId()));
                put("Account Number", bankAccount.getNumber());
                put("Account Currency", bankAccount.getCurrency());
                put("Person ID", String.valueOf(bankAccount.getPersonId()));
            }});
        }
        log.info("Get all bank accounts for person with id = {}", id);
        return result;
    }

    @DeleteMapping("/bank-account/{id}")
    public Map<String, String> deleteBankAccount(@PathVariable String id){
        Repository.deleteBankAccount(Long.parseLong(id));
        Map<String, String> result = new LinkedHashMap<>(){{
            put("Message", "Bank Account id=" + id + " deleted successfully");
        }
        };
        log.info("Delete bank account with id = {}", id);
        return result;
    }

}
