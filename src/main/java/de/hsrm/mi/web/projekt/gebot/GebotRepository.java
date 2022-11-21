package de.hsrm.mi.web.projekt.gebot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GebotRepository extends JpaRepository<Gebot, Long> {
    @Query("select g from Gebot g where g.angebot.id=?1 and g.gebieter.id=?2")
    Optional<Gebot> findByAngebotIdAndBieterId(long angebotId, long BieterId);
}
