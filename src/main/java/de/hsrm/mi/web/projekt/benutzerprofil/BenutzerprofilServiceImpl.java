package de.hsrm.mi.web.projekt.benutzerprofil;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.geo.AdressInfo;
import de.hsrm.mi.web.projekt.geo.GeoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BenutzerprofilServiceImpl implements BenutzerprofilService{

    private BenutzerprofilRepository benutzerprofilRepository;
    private GeoServiceImpl geoService;
    private AngebotRepository angebotRepository;

    @Autowired
    public BenutzerprofilServiceImpl(BenutzerprofilRepository b, GeoServiceImpl g, AngebotRepository a) {
        this.benutzerprofilRepository = b;
        this.geoService = g;
        this.angebotRepository = a;
    }

    @Override
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp) {
        List<AdressInfo> adressInfos = geoService.findeAdressInfo(bp.getAdresse());
        if(adressInfos.isEmpty()) {
            bp.setLat(0);
            bp.setLon(0);
            return benutzerprofilRepository.save(bp);
        }
        AdressInfo adressInfo = adressInfos.get(0);
        bp.setLat(adressInfo.lat());
        bp.setLon(adressInfo.lon());
        //return a managed Benutzerprofil
        return benutzerprofilRepository.save(bp);
    }

    @Override
    public Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id) {
        return benutzerprofilRepository.findById(id);
    }

    @Override
    public List<BenutzerProfil> alleBenutzerProfile() {
        return benutzerprofilRepository.findAll(Sort.by("name"));
    }

    @Override
    public void loescheBenutzerProfilMitId(Long loesch) {
        benutzerprofilRepository.deleteById(loesch);
    }

    @Override
    public void fuegeAngebotHinzu(long id, Angebot angebot) {
        // aktualisiere angebots lat und lon mit dem abholort
        List<AdressInfo> adressInfos = geoService.findeAdressInfo(angebot.getAbholort());
        if(adressInfos.isEmpty()) {
            angebot.setLat(0);
            angebot.setLon(0);
        }
        AdressInfo adressInfo = adressInfos.get(0);
        angebot.setLat(adressInfo.lat());
        angebot.setLon(adressInfo.lon());

        Optional<BenutzerProfil> oBp = holeBenutzerProfilMitId(id);
        BenutzerProfil bp = oBp.orElseThrow();
        bp.addAngebot(angebot);
        angebot.setAnbieter(bp);
        benutzerprofilRepository.save(bp);
    }

    @Override
    public void loescheAngebot(long id) {
        Optional<Angebot> optionalAngebot = angebotRepository.findById(id);
        Angebot angebot = optionalAngebot.orElseThrow();
        BenutzerProfil bp = angebot.getAnbieter();
        bp.deleteAngebot(angebot);
        angebotRepository.delete(angebot);
        benutzerprofilRepository.save(bp);
    }

    @Override
    public List<Angebot> alleAngebote() {
        return angebotRepository.findAll();
    }

    @Override
    public Optional<Angebot> findeAngebotMitId(long angebotid) {
        return angebotRepository.findById(angebotid);
    }
}
