package de.hsrm.mi.web.projekt.projektuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/registrieren")
public class ProjektUserController {
    Logger logger = LoggerFactory.getLogger(ProjektUserController.class);
    ProjektUserService projektUserService;

    public ProjektUserController(ProjektUserServiceImpl projUsrSvc) {
        this.projektUserService = projUsrSvc;
    }


    @GetMapping("")
    public String getRegistrieren(@ModelAttribute("user") ProjektUser projektUser) {
        logger.info("GET Request for 'registrieren'");

        return "projektuser/registrieren";
    }

    @PostMapping("")
    public String postRegistrieren(@Valid @ModelAttribute("user") ProjektUser projektUser, BindingResult registerError, Model m) {
        logger.info("POST Request for 'registrieren'");

        if(registerError.hasErrors()) {
            return "projektuser/registrieren";
        }

        try {
            projektUserService.findeBenutzer(projektUser.getUsername());
            registerError.addError(new FieldError("user", "username", "User existiert schon"));
            return "projektuser/registrieren";
        } catch (ProjektUserServiceException e) {
            m.addAttribute("user", projektUserService.neuenBenutzerAnlegen(projektUser.getUsername(), projektUser.getPassword(), ""));
            return "redirect:/benutzerprofil";
        }

    }
}
