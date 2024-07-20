package com.ead.authuser.validation.impl;

import com.ead.authuser.validation.UsernameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class UsernameConstraintImpl implements ConstraintValidator<UsernameConstraint, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        List<Pair<String, Boolean>> fields = List.of(
            Pair.of("Username with empty strings", username.trim().isEmpty()),
            Pair.of("Username with spaces", username.contains(" "))
        );
        return fields.stream()
            .filter(Pair::getRight)
            .findFirst()
            .map(field -> String.format("Invalid username: %s aren't allowed", field.getLeft()))
            .map(msg -> customErrorMessage(msg, context))
            .orElse(true);
    }

    private boolean customErrorMessage(String errorMsg, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMsg).addConstraintViolation();
        return false;
    }
}
