package com.barracuda.food.controller;

import com.barracuda.food.dto.FormError;
import com.barracuda.food.dto.FormError.GlobalFormError;
import com.barracuda.food.dto.OwnerCreateForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.AdminService;
import com.barracuda.food.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@RequestMapping("/admin")
@RestController
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;
    private final MessageSource messageSource;

    public AdminController(UserService userService, AdminService adminService, MessageSource messageSource) {
        this.userService = userService;
        this.adminService = adminService;
        this.messageSource = messageSource;
    }

    @GetMapping("/users")
    ModelAndView users(Pageable pageable, Model model, PagedResourcesAssembler<User> assembler){
        var page = assembler.toModel(userService.users(pageable));
        model.addAttribute("page",page);

        return new ModelAndView("users");
    }

    @GetMapping("/user/create")
    ModelAndView createUserForm(){
        return new ModelAndView("create_user", Map.of("owner",new OwnerCreateForm()));
    }

    @PostMapping("/owner")
    ModelAndView createOwner(@ModelAttribute("owner") @Valid OwnerCreateForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return new ModelAndView("create_user");
        }

        try{
            adminService.createOwner(form);
        }catch (DataIntegrityViolationException ex){
            if(ex.getMessage().contains("USERS_UNIQUE_EMAIL")){
                bindingResult.addError(new FieldError("owner","email",WebUtils.getMessage(messageSource,"owner.duplicate.email")));
                return new ModelAndView("create_user");
            }
            throw ex;
        }

        redirectAttributes.addFlashAttribute("success",WebUtils.getMessage(messageSource,"owner.created.msg"));
        return new ModelAndView("redirect:/admin/user/create");
    }

}
