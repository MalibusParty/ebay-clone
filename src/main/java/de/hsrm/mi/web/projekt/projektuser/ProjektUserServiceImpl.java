package de.hsrm.mi.web.projekt.projektuser;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProjektUserServiceImpl implements ProjektUserService {

    private PasswordEncoder passwordEncoder;
    private ProjektUserRepository projektUserRepository;
    private BenutzerprofilService benutzerprofilService;

    @Autowired
    public ProjektUserServiceImpl(PasswordEncoder pwdEnc, ProjektUserRepository projUserRep, BenutzerprofilServiceImpl bp) {
        this.passwordEncoder = pwdEnc;
        this.projektUserRepository = projUserRep;
        this.benutzerprofilService = bp;
    }

    @Override
    public ProjektUser neuenBenutzerAnlegen(String username, String klartextpasswort, String role) throws ProjektUserServiceException {

        try {
            findeBenutzer(username);
        } catch (ProjektUserServiceException e) {

            ProjektUser newProjektUser = new ProjektUser(username, passwordEncoder.encode(klartextpasswort), (role != null || !role.equals("")) ? role : "USER");
            BenutzerProfil benutzer = new BenutzerProfil(newProjektUser.getUsername());

            // TODO: is using managed benutzer okay?
            benutzer = benutzerprofilService.speichereBenutzerProfil(benutzer);
            newProjektUser = projektUserRepository.save(newProjektUser);

            benutzer.setProjektUser(newProjektUser);
            newProjektUser.setBenutzerProfil(benutzer);


            return newProjektUser;
        }

        throw new ProjektUserServiceException("Benutzer mit dem Usernamen: " + username + " existiert schon");
    }

    @Override
    public ProjektUser findeBenutzer(String username) throws ProjektUserServiceException {
        return projektUserRepository.findById(username).orElseThrow(() -> new ProjektUserServiceException("Kein Benutzer mit Username: " + username + " gefunden"));
    }
}
