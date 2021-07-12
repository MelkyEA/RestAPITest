package com.mts.test;

import com.mts.test.config.ValidConfig;
import com.mts.test.controller.Controller;
import com.mts.test.model.Person;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(Controller.class)
public class RestControllerTest {

    @Autowired
    private MockMvc mvc;
    private Person p = new Person(1, "Малышев", "Алексей", "Владимирович",
            "01", "3456 123456", "06.08.1996");
    private String jsonContext = "{\"Id\": \"2\"," +
            "\"First Name\": \"Алексей\"," +
            "\"Last Name\": \"Малышев\"," +
            "\"Middle Name\": \"Владимирович\"," +
            "\"Type of document\": \"01\"," +
            "\"Series-Number of document\": \"1234 123456\"," +
            "\"Birthdate\": \"06.08.1996\"}";

    @TestConfiguration
    static class TestConfig{
        @Bean
        public ValidConfig validConfig(){
            return new ValidConfig();
        }
    }

    @Test
    public void testNewPerson() throws Exception{
        mvc.perform(post("/person?lastName=Малышев&firstName=Алексей" +
                "&middleName=Владимирович&typeDocument=01" +
                "&seriesNumber=1234 123456&birthdate=06.08.1996")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().json(jsonContext));
    }

    @Test
    public void testGetPersons() throws Exception{
        mvc.perform(get("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void testNotValidType() throws Exception{
        mvc.perform(post("/person?lastName=Малышев&firstName=Алексей" +
                "&middleName=Владимирович&typeDocument=55" +
                "&seriesNumber=1234 123456&birthdate=06.081996")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().json("{\"Message\": \"Ошибка валидации кода документа\"}"));
    }

    @Test
    public void testCreateBankAccount() throws Exception{
        mvc.perform(post("/person/bank-account?number=0123456789&currency=RUB&id=2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(content().json(
                "{\"Account ID\": \"2\"," +
                "\"Account Number\": \"0123456789\"," +
                "\"Account Currency\": \"RUB\"," +
                "\"Person ID\": \"2\"}"
        ));
    }

    @Test
    public void testPersonBankAccounts() throws Exception{
        mvc.perform(delete("/person/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        mvc.perform(post("/person?lastName=Малышев&firstName=Алексей" +
                "&middleName=Владимирович&typeDocument=01" +
                "&seriesNumber=1234 123456&birthdate=06.08.1996")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        mvc.perform(post("/person/bank-account?number=0123456789&currency=RUB&id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        mvc.perform(get("/person/bank-account/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(content().json(
                "[{\"Account ID\":\"1\"," +
                        "\"Account Number\":\"0123456789\"," +
                        "\"Account Currency\":\"RUB\"," +
                        "\"Person ID\":\"1\"}]"
        ));
    }

    @Test
    public void testNotValidCurrency() throws Exception{
        mvc.perform(post("/person/bank-account?number=0123456789&currency=QWE&id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(content().json(
                "{\"Message\":\"Ошибка валидации кода валют\"}"
        ));
    }

    @Test
    public void testDeleteBankAccount() throws Exception{
        mvc.perform(delete("/person/bank-account/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void testDeletePerson() throws Exception{
        mvc.perform(delete("/person/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
