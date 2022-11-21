package de.hsrm.mi.web.projekt.angebot;


import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.gebot.Gebot;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Angebot {

    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    private String beschreibung;

    private long mindestpreis;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ablaufzeitpunkt;

    private String abholort;

    private double lat;

    private double lon;

    @ManyToOne
    private BenutzerProfil anbieter;

    @OneToMany(mappedBy = "angebot")
    private List<Gebot> gebote = new LinkedList<>();

    public Angebot() {
        this("", 0, LocalDateTime.now(), "");
    }

    public Angebot(String beschreibung, long mindestpreis, LocalDateTime ablaufzeitpunkt, String abholort) {
        this.beschreibung = beschreibung;
        this.mindestpreis = mindestpreis;
        this.ablaufzeitpunkt = ablaufzeitpunkt;
        this.abholort = abholort;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public long getMindestpreis() {
        return mindestpreis;
    }

    public void setMindestpreis(long mindestpreis) {
        this.mindestpreis = mindestpreis;
    }

    public LocalDateTime getAblaufzeitpunkt() {
        return ablaufzeitpunkt;
    }

    public void setAblaufzeitpunkt(LocalDateTime ablaufzeitpunkt) {
        this.ablaufzeitpunkt = ablaufzeitpunkt;
    }

    public String getAbholort() {
        return abholort;
    }

    public void setAbholort(String abholort) {
        this.abholort = abholort;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public BenutzerProfil getAnbieter() {
        return anbieter;
    }

    public void setAnbieter(BenutzerProfil anbieter) {
        this.anbieter = anbieter;
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
}
