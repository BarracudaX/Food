package com.barracuda.food.controller;

import com.barracuda.food.entity.User;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public static String getMessage(MessageSource messageSource,String code){
        return messageSource.getMessage(code,new Object[]{}, LocaleContextHolder.getLocale());
    }
}
