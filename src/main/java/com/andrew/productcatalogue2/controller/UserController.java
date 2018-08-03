package com.andrew.productcatalogue2.controller;


import com.andrew.productcatalogue2.entity.users.Role;
import com.andrew.productcatalogue2.entity.users.User;
import com.andrew.productcatalogue2.security.UserService;
import com.andrew.productcatalogue2.validation.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserValidator userValidator;

    private List<Role> availableRoles;

    @GetMapping("admin/userdetails")
    public ModelAndView getUserDetails(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("user-details");
        User user = userService.findUserByEmail(principal.getName()).get();
        modelAndView.addObject("user", user);
        return modelAndView;
    }


    @GetMapping("/super/adduser")
    public ModelAndView getAddUserForm() {
        ModelAndView modelAndView = new ModelAndView("add-user");
        availableRoles = userService.getRoles();
        modelAndView.addObject("availableRoles" , availableRoles);
        modelAndView.addObject("user", new User());
        return modelAndView;
    }




    @PostMapping("/super/adduser")
    public ModelAndView handleAddUser(@Valid User user, BindingResult bindingResult, RedirectAttributes attributes) {

        ModelAndView modelAndView = new ModelAndView("index");

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("availableRoles" , availableRoles);
            modelAndView.setViewName("add-user");
            return modelAndView;
        }


        try {
            User addedUser = userService.addUser(user);
            attributes.addFlashAttribute("user" , addedUser);
            modelAndView.setViewName("redirect:add-user-confirmation");
        }
        catch(Exception e) {
            log.info("exception caught trying to save user e.getMessage()", e);
            modelAndView.setViewName("error/error");
            modelAndView.addObject("message","Unable to add new user");
        }

        return modelAndView;
    }

    @GetMapping("super/add-user-confirmation")
    public ModelAndView addUserConfirmation(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-details");
        modelAndView.addObject("user", user);
        modelAndView.addObject("message","The new user has been added to the system");
        return modelAndView;
    }



}
