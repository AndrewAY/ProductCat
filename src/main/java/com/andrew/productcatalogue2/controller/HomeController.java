package com.andrew.productcatalogue2.controller;


import com.andrew.productcatalogue2.repository.UserRepository;
import com.andrew.productcatalogue2.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.Optional;


@Controller
@Slf4j
public class HomeController {

    private ProductService productService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model, @ModelAttribute("message") Optional<String> message ) {
        if(message.isPresent()) {
            model.addAttribute("message", message.get());
        }

        return "home";
    }

}
