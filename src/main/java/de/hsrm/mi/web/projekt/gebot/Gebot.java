package de.hsrm.mi.web.projekt.gebot;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Gebot {

    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    @ManyToOne
    private BenutzerProfil gebieter;

    @ManyToOne
    private Angebot angebot;

    private long betrag;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime gebotszeitpunkt;

    public Gebot() {
        this(0, LocalDateTime.now());
    }

    public Gebot(long betrag, LocalDateTime gebotszeitpunkt) {
        this.betrag = betrag;
        this.gebotszeitpunkt = gebotszeitpunkt;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public BenutzerProfil getGebieter() {
        return gebieter;
    }

    public Angebot getAngebot() {
        return angebot;
    }

    public long getBetrag() {
        return betrag;
    }

    public void setGebieter(BenutzerProfil gebieter) {
        this.gebieter = gebieter;
    }

    public void setAngebot(Angebot angebot) {
        this.angebot = angebot;
    }

    public void setBetrag(long betrag) {
        this.betrag = betrag;
    }

    public void setGebotszeitpunkt(LocalDateTime gebotszeitpunkt) {
        this.gebotszeitpunkt = gebotszeitpunkt;
    }

    public LocalDateTime getGebotszeitpunkt() {
        return gebotszeitpunkt;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Gebot)) {
            return false;
        }
        Gebot gebot = (Gebot) o;
        return gebot.getId() == this.getId();
    }
}
