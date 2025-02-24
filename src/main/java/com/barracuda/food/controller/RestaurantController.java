package com.barracuda.food.controller;

import com.barracuda.food.dto.RestaurantCreateForm;
import com.barracuda.food.entity.Restaurant;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.RestaurantService;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@RequestMapping("/restaurant")
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MessageSource messageSource;

    public RestaurantController(RestaurantService restaurantService, MessageSource messageSource) {
        this.restaurantService = restaurantService;
        this.messageSource = messageSource;
    }

    @GetMapping("/all")
    ModelAndView restaurants(Pageable pageable, Authentication authentication, PagedResourcesAssembler<Restaurant> assembler){
        var ownerID = ((User) authentication.getPrincipal()).getId();

        var page = assembler.toModel(restaurantService.restaurants(pageable,ownerID));

        return new ModelAndView("restaurants", Map.of("page",page));
    }

    @GetMapping
    ModelAndView createRestaurantForm(@ModelAttribute("restaurant") RestaurantCreateForm form,Model model){
        return new ModelAndView("create_restaurant");
    }

    @PostMapping
    ModelAndView createRestaurant(@ModelAttribute("restaurant") @Validated RestaurantCreateForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("create_restaurant");
        }

        var ownerID = WebUtils.getUserID();

        try{
            restaurantService.create(form.withOwnerID(ownerID));
        }catch (DataIntegrityViolationException ex){
            if(ex.getMessage().contains("UNIQUE_NAME_PER_OWNER")){
                var message = WebUtils.getMessage(messageSource,"restaurant.duplicate.name.message");
                bindingResult.addError(new FieldError("restaurant","name",message));
                return new ModelAndView("create_restaurant");
            }

            throw ex;
        }

        redirectAttributes.addFlashAttribute("success",WebUtils.getMessage(messageSource,"restaurant.created.success.message"));

        return new ModelAndView("redirect:/restaurant");
    }

}
