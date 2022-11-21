package de.hsrm.mi.web.projekt.benutzerprofil;

import de.hsrm.mi.web.projekt.angebot.Angebot;

import java.util.List;
import java.util.Optional;

public interface BenutzerprofilService {
    BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp);
    Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id);
    List<BenutzerProfil> alleBenutzerProfile();
    void loescheBenutzerProfilMitId(Long loesch);
    void fuegeAngebotHinzu(long id, Angebot angebot);
    void loescheAngebot(long id);
    List<Angebot> alleAngebote();
    Optional<Angebot> findeAngebotMitId(long angebotid);
}
