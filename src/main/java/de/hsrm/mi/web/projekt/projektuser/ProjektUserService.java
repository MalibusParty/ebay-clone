package de.hsrm.mi.web.projekt.projektuser;

public interface ProjektUserService {
    ProjektUser neuenBenutzerAnlegen(String username, String klartextpasswort, String role);
    ProjektUser findeBenutzer(String username);
}
