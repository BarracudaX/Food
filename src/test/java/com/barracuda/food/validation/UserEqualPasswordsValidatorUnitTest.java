package com.barracuda.food.validation;

import com.barracuda.food.dto.CreateUserForm;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEqualPasswordsValidatorUnitTest {

    private UserEqualPasswordsValidator validator = new UserEqualPasswordsValidator();

    @Test
    void shouldReturnTrueWhenBothPasswordAreNull() {
        var form = new CreateUserForm("ANY",null,null,"ANY");

        assertThat(validator.isValid(form,null)).withFailMessage("When both passwords are null, expect to get true.").isTrue();
    }

    @Test
    void shouldReturnFalseWhenOnlyOneOfThePasswordsIsNull() {
        var form = new CreateUserForm("ANY",null,"notNull","ANY");
        assertThat(validator.isValid(form,null))
                .withFailMessage(() -> "When one password is null and the other isn't, expect to return false. Password = "+form.getPassword()+", repeated password = "+form.getRepeatedPassword())
                .isFalse();

        form.setPassword("notNull");
        form.setRepeatedPassword(null);

        assertThat(validator.isValid(form,null))
                .withFailMessage(() -> "When one password is null and the other isn't, expect to return false. Password = "+form.getPassword()+", repeated password = "+form.getRepeatedPassword())
                .isFalse();
    }

    @Test
    void shouldReturnTrueWhenBothPasswordsAreEqual() {
        var form = new CreateUserForm("ANY","EqualPasswords","EqualPasswords","ANY");

        assertThat(validator.isValid(form,null))
                .withFailMessage("When both passwords are equal, expect to get true. Password = "+form.getPassword()+", repeated password = "+form.getRepeatedPassword())
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenPasswordsDoNotMatch() {
        var form = new CreateUserForm("ANY","Password","DiffPassword","ANY");

        assertThat(validator.isValid(form,null))
                .withFailMessage("When passwords do not match, expect to get false. Password = "+form.getPassword()+", repeated password = "+form.getRepeatedPassword())
                .isFalse();
    }
}
