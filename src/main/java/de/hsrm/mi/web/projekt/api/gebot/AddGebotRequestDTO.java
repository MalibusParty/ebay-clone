package de.hsrm.mi.web.projekt.api.gebot;

// JSON z.B. { "benutzerprofilid":2, "angebotid":17, "betrag":4096 }
public record AddGebotRequestDTO(Long benutzerprofilid, Long angebotid, Long betrag) {
}
