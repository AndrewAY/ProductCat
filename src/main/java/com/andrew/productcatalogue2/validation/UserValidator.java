package com.andrew.productcatalogue2.validation;

import com.andrew.productcatalogue2.entity.users.User;
import com.andrew.productcatalogue2.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/** Validates usernames and passwords for new user requests.
 * <p>
 *     Currently checks the request for unique email, password size, and whether
 *     a displayname and a role has been included
 * </p>
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Component
@Slf4j
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        //  must have a displayname
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "Displayname.not.empty");


        //  email must be unique and not empty
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Email.not.empty");
        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "Duplicate.user.email");
        }

        //   password size
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Password.not.empty");
        if (user.getPassword().length() < 4 || user.getPassword().length() > 25) {
            errors.rejectValue("password", "Size.user.password");
        }

        // roles
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roles", "Roles.not.empty");

    }
}