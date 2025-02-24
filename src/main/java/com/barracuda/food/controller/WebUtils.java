package com.barracuda.food.controller;

import com.barracuda.food.dto.FormError;
import com.barracuda.food.entity.User;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public final class WebUtils {

    private WebUtils(){ }

    public static Long getUserID(){
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static void updateSecurityContext(User user,Authentication current){
        var newContext = SecurityContextHolder.createEmptyContext();

        var newAuthentication = switch (current){
            case OneTimeTokenAuthenticationToken _ -> new OneTimeTokenAuthenticationToken(user,current.getAuthorities());
            case UsernamePasswordAuthenticationToken _ -> new UsernamePasswordAuthenticationToken(user,"",current.getAuthorities());
            default -> throw new IllegalStateException("Unknown authentication "+current);
        };

        newContext.setAuthentication(newAuthentication);
        SecurityContextHolder.setContext(newContext);
    }

    public static List<FormError> toFormErrors(BindingResult bindingResult){
        return bindingResult.getAllErrors().stream().map(error -> {
            FormError formError = switch (error) {
                case FieldError fieldError -> new FormError.FieldFormError(fieldError.getField(), fieldError.getDefaultMessage());
                case ObjectError globalError -> new FormError.GlobalFormError(globalError.getDefaultMessage());
            };
            return formError;
        }).toList();
    }

    public static String getMessage(MessageSource messageSource,String code){
        return messageSource.getMessage(code,new Object[]{}, LocaleContextHolder.getLocale());
    }
}
