package de.hsrm.mi.web.projekt.api.gebot;

import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.gebot.GebotService;
import de.hsrm.mi.web.projekt.gebot.GebotServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendInfoService;
import de.hsrm.mi.web.projekt.messaging.BackendInfoServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GebotRestController {

    private GebotService gebotService;
    private BackendInfoService backendInfoService;

    @Autowired
    private SimpMessagingTemplate messaging;

    @Autowired
    public GebotRestController(GebotServiceImpl g, BackendInfoServiceImpl b) {
        this.gebotService = g;
        this.backendInfoService = b;
    }

    @GetMapping(value="/gebot", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetGebotResponseDTO> get_gebote_json() {
        List<GetGebotResponseDTO> gebotResponseDTOList = new ArrayList<>();
        for (Gebot gebot: gebotService.findeAlleGebote()) {
            gebotResponseDTOList.add(GetGebotResponseDTO.from(gebot));
        }
        return gebotResponseDTOList;
    }

    @PostMapping(value="/gebot", consumes = MediaType.APPLICATION_JSON_VALUE)
    public long post_gebot(@RequestBody AddGebotRequestDTO newAddGebotRequestDTO) {
        Gebot gebot = gebotService.bieteFuerAngebot(newAddGebotRequestDTO.benutzerprofilid(), newAddGebotRequestDTO.angebotid(), newAddGebotRequestDTO.betrag());
        GetGebotResponseDTO gebotResponseDTO = GetGebotResponseDTO.from(gebot);
        //backendInfoService.sendInfo("gebot/" + gebotResponseDTO.angebotid(), BackendOperation.CREATE, gebotResponseDTO.gebotid());
        messaging.convertAndSend("/topic/gebot/" + gebotResponseDTO.angebotid(), gebotResponseDTO);
        return gebot.getId();
    }

    @DeleteMapping("/gebot/{id}")
    public void delete_gebot(@PathVariable("id") long id) {
        gebotService.loescheGebot(id);
    }
}
