package com.barracuda.food.controller;

import com.barracuda.food.dto.FormError;
import com.barracuda.food.dto.FormError.FieldFormError;
import com.barracuda.food.dto.FormError.GlobalFormError;
import com.barracuda.food.dto.OwnerCreationForm;
import com.barracuda.food.entity.User;
import com.barracuda.food.service.AdminService;
import com.barracuda.food.service.UserService;
import com.barracuda.food.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/users")
    ModelAndView users(Pageable pageable, Model model, PagedResourcesAssembler<User> assembler){
        var page = assembler.toModel(userService.users(pageable));
        model.addAttribute("page",page);

        return new ModelAndView("users");
    }

    @PostMapping("/owner")
    ResponseEntity<List<FormError>> createOwner(@RequestBody @Valid OwnerCreationForm form, BindingResult bindingResult){

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

        return ResponseEntity.ok(Collections.emptyList());
    }

}
