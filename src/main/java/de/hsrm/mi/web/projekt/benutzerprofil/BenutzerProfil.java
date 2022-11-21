package de.hsrm.mi.web.projekt.benutzerprofil;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.validierung.Bunt;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@Entity
public class BenutzerProfil {
    @Size(min=3,max=60,message="Namenslaenge mindestens {min}, hoechstens {max}")
    @NotNull
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent @NotNull
    private LocalDate geburtsdatum;

    private String adresse;

    @Email(message="{validatedValue} ist keine gueltige E-Mail Adresse")
    private String email;

    @Bunt(message = "{bunt.fehler}")
    @NotNull
    private String lieblingsfarbe;

    private String interessen;

    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    private double lat, lon;

    @OneToMany(mappedBy = "anbieter", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Angebot> angebote = new LinkedList<>();

    @OneToMany(mappedBy = "gebieter")
    private List<Gebot> gebote = new LinkedList<>();

    @OneToOne(mappedBy = "benutzer", cascade = CascadeType.REMOVE)
    private ProjektUser projektUser;

    public BenutzerProfil() {
        this("", LocalDate.of(1,1,1), "", null, "", "");
    }

    public BenutzerProfil(String name) {
        this(name, LocalDate.of(1,1,1), "", null, "", "");
    }

    public BenutzerProfil(String name, LocalDate geburtsdatum, String adresse, String email, String lieblingsfarbe, String interessen) {
        this.name = name;
        this.geburtsdatum = geburtsdatum;
        this.adresse = adresse;
        this.email = email;
        this.lieblingsfarbe = lieblingsfarbe;
        this.interessen = interessen;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getGeburtsdatum() {
        return this.geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLieblingsfarbe() {
        return this.lieblingsfarbe;
    }

    public void setLieblingsfarbe(String lieblingsfarbe) {
        this.lieblingsfarbe = lieblingsfarbe;
    }

    public String getInteressen() {
        return this.interessen;
    }

    public void setInteressen(String interessen) {
        this.interessen = interessen;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public long getVersion() {
        return version;
    }

    public long getId() {
        return id;
    }

    public void addAngebot(Angebot angebot) {
        angebote.add(angebot);
    }

    public void deleteAngebot(Angebot angebot) {
        angebote.remove(angebot);
    }

    public List<Angebot> getAngebote() {
        return angebote;
    }

    public List<Gebot> getGebote() {
        return gebote;
    }

    public void addGebot(Gebot gebot) {
        gebote.add(gebot);
    }

    public void deleteGebot(Gebot gebot) {
        gebote.remove(gebot);
    }

    public ProjektUser getProjektUser() {
        return projektUser;
    }

    public void setProjektUser(ProjektUser projektUser) {
        this.projektUser = projektUser;
    }

    public LinkedList<String> getInteressenListe() {
        LinkedList<String> interessenListe = new LinkedList<>();
        if(!interessen.equals("")) {
            String[] interessenArr = interessen.split(",");
            for (String interesse : interessenArr) {
                interesse = interesse.strip();
                interessenListe.add(interesse);
            }
        }
        return interessenListe;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BenutzerProfil)) {
            return false;
        }
        BenutzerProfil benutzerprofil = (BenutzerProfil) o;
        return benutzerprofil.getName().equals(this.getName()) && benutzerprofil.getGeburtsdatum().equals(this.getGeburtsdatum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, geburtsdatum);
    }

    @Override
    public String toString() {
        return "BenutzerProfil{" +
                "name='" + name + '\'' +
                ", geburtsdatum=" + geburtsdatum +
                ", adresse='" + adresse + '\'' +
                ", email='" + email + '\'' +
                ", lieblingsfarbe='" + lieblingsfarbe + '\'' +
                ", interessen='" + interessen + '\'' +
                ", id=" + id +
                ", version=" + version +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }


}
