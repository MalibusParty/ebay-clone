package de.hsrm.mi.web.projekt.gebot;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendInfoService;
import de.hsrm.mi.web.projekt.messaging.BackendInfoServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GebotServiceImpl implements GebotService {

    private GebotRepository gebotRepository;
    private BenutzerprofilService benutzerprofilService;
    private BackendInfoService backendInfoService;



    @Autowired
    public GebotServiceImpl(GebotRepository g, BenutzerprofilServiceImpl b, BackendInfoServiceImpl backendInfoService) {
        this.gebotRepository = g;
        this.benutzerprofilService = b;
        this.backendInfoService = backendInfoService;
    }

    @Override
    public List<Gebot> findeAlleGebote() {
        return gebotRepository.findAll();
    }

    @Override
    public List<Gebot> findeAlleGeboteFuerAngebot(long angebotid) {
        Optional<Angebot> optionalAngebot = benutzerprofilService.findeAngebotMitId(angebotid);
        if(optionalAngebot.isEmpty()) {
            return new LinkedList<>();
        }
        Angebot angebot = optionalAngebot.get();
        return angebot.getGebote();
    }

    @Override
    public Gebot bieteFuerAngebot(long benutzerprofilid, long angebotid, long betrag) {
        Optional<Gebot> optionalGebot = gebotRepository.findByAngebotIdAndBieterId(angebotid, benutzerprofilid);
        Gebot gebot;
        if(optionalGebot.isPresent()) {
            gebot = optionalGebot.get();
            gebot.setBetrag(betrag);
            gebot.setGebotszeitpunkt(LocalDateTime.now());
        } else {
            gebot = new Gebot(betrag, LocalDateTime.now());
            Optional<Angebot> optionalAngebot = benutzerprofilService.findeAngebotMitId(angebotid);
            Optional<BenutzerProfil> optionalBenutzerProfil = benutzerprofilService.holeBenutzerProfilMitId(benutzerprofilid);
            if(optionalAngebot.isEmpty() || optionalBenutzerProfil.isEmpty()) {
                return null;
            }
            Angebot angebot = optionalAngebot.get();
            gebot.setAngebot(angebot);
            angebot.addGebot(gebot);

            BenutzerProfil benutzerProfil = optionalBenutzerProfil.get();
            gebot.setGebieter(benutzerProfil);
            benutzerProfil.addGebot(gebot);
        }
        gebot = gebotRepository.save(gebot);
        return gebot;
    }

    @Override
    public void loescheGebot(long gebotid) {
        Optional<Gebot> optionalGebot = gebotRepository.findById(gebotid);
        Gebot gebot = optionalGebot.orElseThrow();
        BenutzerProfil benutzerProfil = gebot.getGebieter();
        Angebot angebot = gebot.getAngebot();
        benutzerProfil.deleteGebot(gebot);
        angebot.deleteGebot(gebot);
        gebotRepository.delete(gebot);
        // vllt noch andere repos saven
    }
}
