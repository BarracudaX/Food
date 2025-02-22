package com.barracuda.food.controller;

import com.barracuda.food.dto.FormError;
import com.barracuda.food.dto.FormError.FieldFormError;
import com.barracuda.food.dto.FormError.GlobalFormError;
import com.barracuda.food.dto.OwnerCreationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.AdminService;
import com.barracuda.food.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @PostMapping("/owner")
    ResponseEntity<Object> createOwner(@RequestBody @Valid OwnerCreationForm form, BindingResult bindingResult){

        var errors = bindingResult.getAllErrors().stream().map(error -> {
            FormError formError = switch (error) {
                case FieldError fieldError -> new FieldFormError(fieldError.getField(), fieldError.getDefaultMessage());
                case ObjectError globalError -> new GlobalFormError(globalError.getDefaultMessage());
            };
            return formError;
        }).toList();

        if(!errors.isEmpty()){
            return ResponseEntity.badRequest().body(errors);
        }

        adminService.createOwner(form);

        var successMsg = messageSource.getMessage("owner.created.msg",new Object[]{}, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(successMsg);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<List<FormError>> dataIntegrityViolationHandler(DataIntegrityViolationException ex){
        var errorMsg = messageSource.getMessage("owner.duplicate.email",new Object[]{}, LocaleContextHolder.getLocale());
        if(ex.getMessage().contains("USERS_UNIQUE_EMAIL")){
            return ResponseEntity.badRequest().body(List.of(new GlobalFormError(errorMsg)));
        }
        throw ex;
    }
}
