package com.barracuda.food;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.util.stream.Stream;

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class AbstractTest {

    public static String[] emptyStrings(){
        return new String[]{null,"","  "," "};
    }

    public static String[] invalidEmails(){
        return new String[]{
                "emmanuel.hibernate.org",
        "emma nuel@hibernate.org",
        "emma(nuel@hibernate.org",
        "emmanuel@",
        "emma\nnuel@hibernate.org",
        "emma@nuel@hibernate.org",
        "emma@nuel@.hibernate.org",
        "Just a string",
        "string",
        "me@",
        "@example.com",
        "me.@example.com",
        ".me@example.com",
        "me@example..com",
        "me\\@example.com",
        "Abc.example.com",
        "A@b@c@example.com",
        "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
        "just\"not\"right@example.com",
        "this is\"not\\allowed@example.com",
        "this\\ still\\\"not\\\\allowed@example.com",
        "john..doe@example.com",
        "john.doe@example..com"
        };
    }

    public static String[] invalidPasswords(){
        return new String[]{ "7Charsl","nodigitspass","12345678" };
    }
}
