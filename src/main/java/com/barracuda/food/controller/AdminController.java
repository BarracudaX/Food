package com.barracuda.food.controller;

import com.barracuda.food.dto.CreateUserForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.AdminService;
import com.barracuda.food.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return new ModelAndView("create_owner", Map.of("owner",new CreateUserForm()));
    }

    @PostMapping("/owner")
    ModelAndView createOwner(@ModelAttribute("owner") @Valid CreateUserForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return new ModelAndView("create_owner");
        }

        try{
            adminService.createOwner(form);
        }catch (DataIntegrityViolationException ex){
            if(ex.getMessage().contains("USERS_UNIQUE_EMAIL")){
                bindingResult.addError(new FieldError("owner","email",WebUtils.getMessage(messageSource,"owner.duplicate.email")));
                return new ModelAndView("create_owner");
            }
            throw ex;
        }

        redirectAttributes.addFlashAttribute("success",WebUtils.getMessage(messageSource,"owner.created.msg"));
        return new ModelAndView("redirect:/admin/user/create");
    }

}
