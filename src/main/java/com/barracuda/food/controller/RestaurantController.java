package com.barracuda.food.controller;

import com.barracuda.food.dto.FormError;
import com.barracuda.food.dto.FormError.GlobalFormError;
import com.barracuda.food.dto.RestaurantCreateForm;
import com.barracuda.food.service.RestaurantService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static com.barracuda.food.controller.ViewController.RESTAURANT_COMMAND_OBJECT_NAME;

@RequestMapping("/restaurant")
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MessageSource messageSource;

    public RestaurantController(RestaurantService restaurantService, MessageSource messageSource) {
        this.restaurantService = restaurantService;
        this.messageSource = messageSource;
    }

    @PostMapping
    ModelAndView createRestaurant(@ModelAttribute(RESTAURANT_COMMAND_OBJECT_NAME) @Validated RestaurantCreateForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        var errors = WebUtils.toFormErrors(bindingResult);

        if(!errors.isEmpty()){
            return new ModelAndView("create_restaurant");
        }

        var ownerID = WebUtils.getUserID();

        try{
            restaurantService.create(form.withOwnerID(ownerID));
        }catch (DataIntegrityViolationException ex){
            if(ex.getMessage().contains("UNIQUE_NAME_PER_OWNER")){
                var message = WebUtils.getMessage(messageSource,"restaurant.duplicate.name.message");
                bindingResult.addError(new FieldError(RESTAURANT_COMMAND_OBJECT_NAME,"name",message));
                return new ModelAndView("create_restaurant");
            }

            throw ex;
        }

        redirectAttributes.addAttribute("success",WebUtils.getMessage(messageSource,"restaurant.created.success.message"));

        return new ModelAndView("redirect:/create_restaurant");
    }

}
