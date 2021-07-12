package com.mts.test.repos;

import com.mts.test.config.ValidConfig;
import com.mts.test.controller.Controller;
import com.mts.test.model.BankAccount;
import com.mts.test.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.sql.*;

@Component
public class Repository {
    private static Connection h2con;
    private static Statement query;
    static {
        try {
            h2con = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            query = h2con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static final Logger log = LoggerFactory.getLogger(Repository.class);

    public Repository() throws SQLException {
    }

    public static void createTable() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("Class not found: {}", e);
            return;
        }
        String sql = "";
        try{
            sql = "DROP TABLE BankAccount CASCADE";
            query.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
            log.error("Невозможно удалить таблицу BankAccount");
        }
        try{
            sql = "DROP TABLE Person CASCADE";
            query.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
            log.error("Невозможно удалить таблицу Person");
        }
        try {
            sql = "CREATE TABLE Person (id BIGINT not null AUTO_INCREMENT," +
                    "first_name VARCHAR(45), " +
                    "last_name VARCHAR(45), " +
                    "middle_name VARCHAR(45), " +
                    "type_document VARCHAR(45), " +
                    "series_number VARCHAR(45), " +
                    "birthdate VARCHAR(45))";
            query.execute(sql);
        }catch (SQLException e){
            log.error("Таблица Person уже создана");
        }
        try {
            sql = "CREATE TABLE BankAccount (id BIGINT not null AUTO_INCREMENT, " +
                    "number VARCHAR(45), " +
                    "currency VARCHAR(45), " +
                    "person_id BIGINT NULL," +
                    "CONSTRAINT fk_bankAccount_person_ID FOREIGN KEY(person_id) references Person(id) )";
            query.execute(sql);
        }catch (SQLException e){
            log.error("Таблица BankAccount уже создана");
        }
    }

    public static List<Person> findAll(){
        List<Person> result = new ArrayList<>();
        try {
            ResultSet resultSet = query.executeQuery("SELECT * FROM Person");
            while (resultSet.next()){
                result.add(new Person(
                        resultSet.getLong(1), resultSet.getString(3),
                        resultSet.getString(2), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getString(6),
                        resultSet.getString(7)
                        ));
            }
        } catch (SQLException e) {
           log.error("Cant find all in person: {}", e);
        }
        return result;
    }

    public static Person createNew(String lastName, String firstName, String middleName,
                                   String typeDocument, String seriesNumber, String birthdate){
        PreparedStatement preparedStatement = null;
        Person person = null;
        try {
            preparedStatement = h2con.prepareStatement("INSERT INTO Person (first_name, " +
                    "last_name, middle_name, type_document, series_number, birthdate) " +
                    "VALUES ( ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, middleName);
            preparedStatement.setString(4, typeDocument);
            preparedStatement.setString(5, seriesNumber);
            preparedStatement.setString(6, birthdate);
            preparedStatement.executeUpdate();
            //Получаем id нового person
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            //Добавляем в модель объект нового person
            resultSet = query.executeQuery("SELECT * FROM Person WHERE id = " + resultSet.getLong(1));
            resultSet.next();
            person = new Person(
                    resultSet.getInt(1), resultSet.getString(3),
                    resultSet.getString(2), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6),
                    resultSet.getString(7)
            );
        } catch (SQLException throwables) {
            log.error("Cant create new in person: {}", throwables);
            throwables.printStackTrace();
        }
        return person;
    }

    public static void deletePerson(Long id){
        try {
            query.executeUpdate("DELETE FROM Person WHERE id = " + id);
        } catch (SQLException throwables) {
            log.error("Cant delete with id = {} in person: {}", id, throwables);
            throwables.printStackTrace();
        }
    }

    public static BankAccount createBankAccount(String number, String currency, Long personId){
        String sql = "INSERT INTO BankAccount (number, currency, person_id) " +
                "VALUES (?, ?, ?)";
        BankAccount bankAccount = null;
        try {
            PreparedStatement preparedStatement = h2con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, currency);
            preparedStatement.setLong(3, personId);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            resultSet = query.executeQuery("SELECT * FROM BankAccount WHERE id = " + resultSet.getLong(1));
            resultSet.next();
            bankAccount = new BankAccount(resultSet.getLong(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getLong(4));
        } catch (SQLException throwables) {
            log.error("Cant create new in bankAccount: {}", throwables);
            throwables.printStackTrace();
        }
        return bankAccount;
    }

    public static List<BankAccount> getPersonBankAccounts(Long personId) {
        String sql = "SELECT * FROM BankAccount WHERE person_id = " + personId;
        List<BankAccount> result = new ArrayList<>();
        try {
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()){
                result.add(new BankAccount(resultSet.getLong(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getLong(4)));
            }
        } catch (SQLException throwables) {
            log.error("Cant find bank accounts for person with id = {} in bankAccount: {}", personId, throwables);
            throwables.printStackTrace();
        }
        return result;
    }

    public static void deleteBankAccount(Long id){
        try {
            query.execute("DELETE FROM BankAccount WHERE id = " + id);
        } catch (SQLException throwables) {
            log.error("Cant delete with id = {} in bankAccount: {}", id, throwables);
            throwables.printStackTrace();
        }
    }
}
