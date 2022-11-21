package de.hsrm.mi.web.projekt.api.benutzer;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.gebot.Gebot;

import java.time.LocalDateTime;
import java.util.List;

public record GetAngebotResponseDTO(
        long angebotid,
        String beschreibung,
        long anbieterid,
        String anbietername,
        long mindestpreis,
        LocalDateTime ablaufzeitpunkt,
        String abholort,
        double lat,
        double lon,
        long topgebot,
        long gebote) {

    public static GetAngebotResponseDTO from(Angebot a) {
        BenutzerProfil benutzerProfil = a.getAnbieter();
        List<Gebot> gebote = a.getGebote();
        long topGebot = 0;
        for(Gebot gebot: gebote) {
            if(gebot.getBetrag() > topGebot) {
                topGebot = gebot.getBetrag();
            }
        }

        return new GetAngebotResponseDTO(
                a.getId(),
                a.getBeschreibung(),
                benutzerProfil.getId(),
                benutzerProfil.getName(),
                a.getMindestpreis(),
                a.getAblaufzeitpunkt(),
                a.getAbholort(),
                a.getLat(),
                a.getLon(),
                topGebot,
                gebote.size()
                );
    }
}
