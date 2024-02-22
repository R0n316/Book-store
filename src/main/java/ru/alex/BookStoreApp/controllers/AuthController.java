package ru.alex.BookStoreApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alex.BookStoreApp.models.Person;
import ru.alex.BookStoreApp.services.PersonDetailsService;
import ru.alex.BookStoreApp.services.RegistrationService;
import ru.alex.BookStoreApp.util.PersonValidator;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;

    private final PersonValidator personValidator;

    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator,
                          PersonDetailsService personDetailsService) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person){
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person,bindingResult);
        if(bindingResult.hasErrors()){
            return "/auth/registration";
        }
        registrationService.register(person);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth-person")
    @ResponseBody
    public Map<String, Integer> getAuthenticatedPersonId(){
        Person authenticatedPerson = personDetailsService.getAuthenticatedPerson();
        if(authenticatedPerson!=null){
            return Map.of("personId",authenticatedPerson.getPersonId());
        }
        return Map.of("personId",0);

    }
}
