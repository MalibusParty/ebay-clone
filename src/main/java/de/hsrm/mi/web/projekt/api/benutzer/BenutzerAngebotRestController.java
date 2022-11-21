package de.hsrm.mi.web.projekt.api.benutzer;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilServiceImpl;
import de.hsrm.mi.web.projekt.gebot.Gebot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BenutzerAngebotRestController {

    private BenutzerprofilService benutzerprofilService;

    @Autowired
    public BenutzerAngebotRestController(BenutzerprofilServiceImpl b) {
        this.benutzerprofilService = b;
    }

    @GetMapping(value="/angebot", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetAngebotResponseDTO> get_angebote_json() {
        List<GetAngebotResponseDTO> angebotResponseDTOList = new ArrayList<>();
        for(Angebot angebot: benutzerprofilService.alleAngebote()) {
            angebotResponseDTOList.add(GetAngebotResponseDTO.from(angebot));
        }
        return angebotResponseDTOList;
    }

    @GetMapping(value="/angebot/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAngebotResponseDTO get_angebot_json(@PathVariable("id") long id) {
        Angebot angebot = benutzerprofilService.findeAngebotMitId(id).orElseThrow();
        return GetAngebotResponseDTO.from(angebot);
    }

    @GetMapping(value = "/angebot/{id}/gebot", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetGebotResponseDTO> get_gebote_of_angebot_json(@PathVariable("id") long id) {
        List<GetGebotResponseDTO> geboteResponseDTOList = new ArrayList<>();
        Angebot angebot = benutzerprofilService.findeAngebotMitId(id).orElseThrow();
        for(Gebot gebot: angebot.getGebote()) {
            geboteResponseDTOList.add(GetGebotResponseDTO.from(gebot));
        }
        return geboteResponseDTOList;
    }

    @GetMapping(value = "/benutzer/{id}/angebot", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetAngebotResponseDTO> get_angebote_of_benutzer_json(@PathVariable("id") long id) {
        List<GetAngebotResponseDTO> angebotResponseDTOList = new ArrayList<>();
        BenutzerProfil benutzerProfil = benutzerprofilService.holeBenutzerProfilMitId(id).orElseThrow();
        for(Angebot angebot: benutzerProfil.getAngebote()) {
            angebotResponseDTOList.add(GetAngebotResponseDTO.from(angebot));
        }
        return angebotResponseDTOList;
    }
}
