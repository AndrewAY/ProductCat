package com.andrew.productcatalogue2.controller;

import com.andrew.productcatalogue2.entity.users.User;
import com.andrew.productcatalogue2.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/")
@Slf4j
public class LoginController {


    @Autowired
    UserService userService;



    @GetMapping("/login")
    public ModelAndView login(RedirectAttributes attributes) {

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is already logged in */
            attributes.addFlashAttribute("message", "You are already logged in!");
            modelAndView.setViewName("redirect:/home");
            return modelAndView;
        }

        /* no users logged in, so continue to login form */
        modelAndView.setViewName("login");
        return modelAndView;
    }





    /* will be redirected here if user has logged in from the nav bar or menu*/
    @GetMapping("login-home")
    public ModelAndView loginConfirmation(Principal principal) {

        ModelAndView modelAndView = new ModelAndView("user-details");
        User user = userService.findUserByEmail(principal.getName()).get();

        modelAndView.addObject("user", user);
        modelAndView.addObject("loginConfirmation", "true");
        return modelAndView;
    }




}
