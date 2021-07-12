package com.mts.test.model;

public class BankAccount {
    private Long accountId;
    private String number;
    private String currency;
    private Long personId;

    public BankAccount(long accountId, String number, String currency, Long personId) {
        this.accountId = accountId;
        this.number = number;
        this.currency = currency;
        this.personId = personId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccount_id(long accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id = " + accountId +
                ", number = " + number +
                ", currency = " + currency  +
                ", personId = " + personId +
                '}';
    }
}
