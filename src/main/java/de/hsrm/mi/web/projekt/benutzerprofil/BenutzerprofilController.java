package de.hsrm.mi.web.projekt.benutzerprofil;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.messaging.BackendInfoService;
import de.hsrm.mi.web.projekt.messaging.BackendInfoServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;
import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserService;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserServiceException;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes(names = {"profil"})
@RequestMapping("/")
public class BenutzerprofilController {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);
    BenutzerprofilService benutzerprofilService;
    BackendInfoService backendInfoService;
    ProjektUserService projektUserService;

    public BenutzerprofilController(BenutzerprofilServiceImpl bpService, BackendInfoServiceImpl backendInfoService, ProjektUserServiceImpl projUsrSrvc) {
        this.benutzerprofilService = bpService;
        this.backendInfoService = backendInfoService;
        this.projektUserService = projUsrSrvc;
    }

    @ModelAttribute("profil")
    public void initProfil(Model m, Locale locale, Principal prinzipal) {
        if(prinzipal != null) {
            try {
                String loginname = prinzipal.getName();
                ProjektUser projektUser = projektUserService.findeBenutzer(loginname);
                BenutzerProfil benutzer = projektUser.getBenutzerProfil();
                m.addAttribute("profil", benutzer);
            } catch(ProjektUserServiceException e) {
                m.addAttribute("profil", new BenutzerProfil());
            }
        } else {
            m.addAttribute("profil", new BenutzerProfil());
        }


        m.addAttribute("sprache", locale.getDisplayLanguage());
    }
    
    @GetMapping("/benutzerprofil")
    public String profilGET(Model m, @ModelAttribute("profil") BenutzerProfil benutzerprofil) {

        logger.info("GET Request for 'profilansicht' {}", benutzerprofil);

        LinkedList<String> interessenListe = benutzerprofil.getInteressenListe();
        logger.info("InteressenListe Output:" + interessenListe.toString());

        m.addAttribute("name", benutzerprofil.getName());
        m.addAttribute("geburtsdatum", benutzerprofil.getGeburtsdatum().format(DateTimeFormatter.ISO_DATE));
        m.addAttribute("adresse", benutzerprofil.getAdresse());
        m.addAttribute("email", benutzerprofil.getEmail());
        m.addAttribute("lieblingsfarbe", benutzerprofil.getLieblingsfarbe());
        m.addAttribute("interessenListe", benutzerprofil.getInteressenListe());
        m.addAttribute("latitude", benutzerprofil.getLat());
        m.addAttribute("longitude", benutzerprofil.getLon());

        return "benutzerprofil/profilansicht";
    }

    @GetMapping("/benutzerprofil/bearbeiten")
    public String profilEditGet(@ModelAttribute("profil") BenutzerProfil benutzerprofil) {

        logger.info("GET Request for 'profileditor' {}", benutzerprofil);

        return "benutzerprofil/profileditor";
    }

    @PostMapping("/benutzerprofil/bearbeiten")
    public String profilEditPost(@Valid @ModelAttribute("profil") BenutzerProfil benutzerprofil, BindingResult benutzerprofilError,
                                 Model m) {

        logger.info("POST Request for 'bearbeiten'");

        if(benutzerprofilError.hasErrors()) {
            return "benutzerprofil/profileditor";
        }

        m.addAttribute("profil", benutzerprofilService.speichereBenutzerProfil(benutzerprofil));

        return "redirect:/benutzerprofil";
    }

    @GetMapping("/benutzerprofil/clearsession")
    public String sparLogout(SessionStatus sessionStatus) {
        logger.info("GET Request for 'clearsession");
        sessionStatus.setComplete();
        return "redirect:/benutzerprofil";
    }

    @GetMapping("/benutzerprofil/liste")
    public String profilListeGet(@RequestParam(required = false) Long id, @RequestParam(required = false) String op, Model m) {

        logger.info("GET Request for 'profilliste'");

        if(op != null) {
            switch(op) {
                case "loeschen":
                    benutzerprofilService.loescheBenutzerProfilMitId(id);
                    return "redirect:/benutzerprofil/liste";
                case "bearbeiten":
                        m.addAttribute("profil", benutzerprofilService.holeBenutzerProfilMitId(id).orElse(new BenutzerProfil()));
                    return "redirect:/benutzerprofil/bearbeiten";
            }
        }

        m.addAttribute("profilliste", benutzerprofilService.alleBenutzerProfile());
        return "benutzerprofil/profilliste";
    }

    @GetMapping("/benutzerprofil/angebot")
    public String angebotFormGet(Model m) {
        logger.info("GET Request for 'angebot");

        m.addAttribute("angebot", new Angebot());
        return "benutzerprofil/angebotsformular";
    }

    @PostMapping("/benutzerprofil/angebot")
    public String angebotFormPost(@ModelAttribute("profil") BenutzerProfil benutzerProfil, @ModelAttribute("angebot") Angebot angebot, Model m) {
        logger.info("POST request for 'angebot");

        benutzerprofilService.fuegeAngebotHinzu(benutzerProfil.getId(), angebot);
        m.addAttribute("profil", benutzerprofilService.holeBenutzerProfilMitId(benutzerProfil.getId()).orElse(new BenutzerProfil()));

        backendInfoService.sendInfo("angebot", BackendOperation.CREATE, angebot.getId());

        return "redirect:/benutzerprofil";
    }

    @GetMapping("/benutzerprofil/angebot/{id}/del")
    public String deleteAngebot(@PathVariable String id, @ModelAttribute("profil") BenutzerProfil benutzerProfil,Model m) {

        logger.info("GET request for deleting angebot with id:{}", id);

        long angebotId = Long.parseLong(id);
        benutzerprofilService.loescheAngebot(angebotId);
        m.addAttribute("profil", benutzerprofilService.holeBenutzerProfilMitId(benutzerProfil.getId()).orElse(new BenutzerProfil()));

        backendInfoService.sendInfo("angebot", BackendOperation.DELETE, angebotId);

        return "redirect:/benutzerprofil";
    }
}
