package de.hsrm.mi.web.projekt.api.gebot;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.gebot.Gebot;

import java.time.LocalDateTime;

public record GetGebotResponseDTO(
        Long gebotid,
        Long gebieterid, String gebietername,
        Long angebotid, String angebotbeschreibung,
        Long betrag,
        LocalDateTime gebotzeitpunkt) {

    public static GetGebotResponseDTO from(Gebot g) {
        BenutzerProfil benutzerProfil = g.getGebieter();
        Angebot angebot = g.getAngebot();

        return new GetGebotResponseDTO(
                g.getId(),
                benutzerProfil.getId(), benutzerProfil.getName(),
                angebot.getId(), angebot.getBeschreibung(),
                g.getBetrag(),
                g.getGebotszeitpunkt());
    }
}
