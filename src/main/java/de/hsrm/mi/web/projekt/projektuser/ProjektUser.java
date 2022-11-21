package de.hsrm.mi.web.projekt.projektuser;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class ProjektUser {

    @Id
    @Size(min=3,max=100,message = "Benutzername muss zwischen {min} und {max} Zeichen lang sein")
    @NotBlank
    private String username;

    @Size(min=3,max=100,message = "Passwort muss zwischen {min} und {max} Zeichen lang sein")
    @NotBlank
    private String password;

    private String role;

    @OneToOne
    private BenutzerProfil benutzer;

    public ProjektUser() {
        this.role = "";
    }

    public ProjektUser(String username, String password) {
        this(username, password, "");
    }

    public ProjektUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BenutzerProfil getBenutzerProfil() {
        return benutzer;
    }

    public void setBenutzerProfil(BenutzerProfil benutzerProfil) {
        this.benutzer = benutzerProfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjektUser that = (ProjektUser) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
