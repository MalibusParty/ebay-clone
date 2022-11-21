package de.hsrm.mi.web.projekt.geo;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedList;
import java.util.List;

@Service
public class GeoServiceImpl implements GeoService{
    @Override
    public List<AdressInfo> findeAdressInfo(String adresse) {
        if(adresse==null || adresse.equals("")) {
            return new LinkedList<AdressInfo>();
        }
        // Beispiel-Query "https://nominatim.openstreetmap.org/search?q=Schafhofweg+9,+Lohr&format=json"
        String searchAdresse = adresse.replaceAll("\s", "+");
        WebClient webClient = WebClient.create("https://nominatim.openstreetmap.org");
        List<AdressInfo> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", searchAdresse)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(AdressInfo.class)
                .collectList()
                .block();

        return response;
    }

    @Override
    public double abstandInKm(AdressInfo adr1, AdressInfo adr2) {
        final double ERDRADIUS_KM = 6370;
        var grad2rad = Math.PI / 180.0;
        var phi_a = adr1.lat() * grad2rad;
        var lambda_a = adr1.lon() * grad2rad;
        var phi_b = adr2.lat() * grad2rad;
        var lambda_b = adr2.lon() * grad2rad;
        var zeta = Math.acos(Math.sin(phi_a) * Math.sin(phi_b)
                + Math.cos(phi_a)*Math.cos(phi_b)*Math.cos(lambda_b-lambda_a));
        return zeta * ERDRADIUS_KM;
    }

    @Override
    public double abstandKmNachGrad(double abstand) {
        return abstand / 111.139;
    }
}
