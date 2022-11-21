import { reactive, readonly, ref } from "vue";

import { Client } from '@stomp/stompjs';
const wsurl = `ws://${window.location.host}/stompbroker`

import { useLogin } from '@/services/useLogin'
import type { IBackendInfoMessage } from "./IBackendInfoMessage";
const { logindata } = useLogin()

export function useGebot(angebotid: number) {
    /*
     * Mal ein Beispiel für CompositionFunction mit Closure/lokalem State,
     * um parallel mehrere *verschiedene* Versteigerungen managen zu können
     * (Gebot-State ist also *nicht* Frontend-Global wie Angebot(e)-State)
     */

    // STOMP-Destination
    const DEST = `/topic/gebot/${angebotid}`

    ////////////////////////////////

    // entspricht GetGebotResponseDTO.java aus dem Spring-Backend
    interface IGetGebotResponseDTO {
        gebotid: number,
        gebieterid: number,
        gebietername: string,
        angebotid: number,
        angebotbeschreibung: string,
        betrag: number,
        gebotzeitpunkt: string // kommt als ISO-DateTime String-serialisiert an!
    }

    // Basistyp für gebotState
    interface IGebotState {
        angebotid: number,              // ID des zugehörigen Angebots
        topgebot: number,               // bisher höchster gebotener Betrag
        topbieter: string,              // Name des Bieters, der das aktuelle topgebot gemacht hat
        gebotliste: IGetGebotResponseDTO[], // Liste der Gebote, wie vom Backend geliefert
        receivingMessages: boolean,     // Status, ob STOMP-Messageempfang aktiv ist oder nicht
        errormessage: string            // (aktuelle) Fehlernachricht oder Leerstring
    }



    const gebotState = reactive<IGebotState> ({
        /* reaktives Objekt auf Basis des Interface <IGebotState> */
        angebotid: 0,
        topgebot: 0,
        topbieter: "",
        gebotliste: [],
        receivingMessages: false,
        errormessage: ""
    });


    function processGebotDTO(gebotDTO: IGetGebotResponseDTO) {
        const dtos = JSON.stringify(gebotDTO)
        console.log(`processGebot(${dtos})`)

        /*
         * suche Angebot für 'gebieter' des übergebenen Gebots aus der gebotliste (in gebotState)
         * falls vorhanden, hat der User hier schon geboten und das Gebot wird nur aktualisiert (Betrag/Gebot-Zeitpunkt)
         * falls nicht, ist es ein neuer Bieter für dieses Angebot und das DTO wird vorne in die gebotliste des State-Objekts aufgenommen
         */
        angebotid = gebotDTO.angebotid;
        const gebieterDTO = gebotState.gebotliste.find(ele => ele.gebieterid === gebotDTO.gebieterid)
        if(gebieterDTO === undefined) {
            gebotState.gebotliste.unshift(gebotDTO);
            console.log('added gebieter to list');
        } else {
            gebieterDTO.betrag = gebotDTO.betrag;
            gebieterDTO.gebotzeitpunkt = gebotDTO.gebotzeitpunkt;
        }


        /*
         * Falls gebotener Betrag im DTO größer als bisheriges topgebot im State,
         * werden topgebot und topbieter (der Name, also 'gebietername' aus dem DTO)
         * aus dem DTO aktualisiert
         */
        if(gebotDTO.betrag > gebotState.topgebot) {
            gebotState.topgebot = gebotDTO.betrag;
            gebotState.topbieter = gebotDTO.gebietername;
        }

    }


    function receiveGebotMessages() {
        /*
         * analog zu Message-Empfang bei Angeboten
         * wir verbinden uns zur brokerURL (s.o.),
         * bestellen Nachrichten von Topic DEST (s.o.)
         * und rufen die Funktion processGebotDTO() von oben
         * für jede neu eingehende Nachricht auf diesem Topic auf.
         * Eingehende Nachrichten haben das Format IGetGebotResponseDTO (s.o.)
         * Die Funktion aktiviert den Messaging-Client nach fertiger Einrichtung.
         * 
         * Bei erfolgreichem Verbindungsaubau soll im State 'receivingMessages' auf true gesetzt werden,
         * bei einem Kommunikationsfehler auf false 
         * und die zugehörige Fehlermeldung wird in 'errormessage' des Stateobjekts geschrieben
         */

        const wsurl = `ws://${window.location.host}/stompbroker`;
        
        const stompclient = new Client({ brokerURL: wsurl });
        stompclient.onWebSocketError = (event) => {
            console.log(`ERROR: WebSocket-Error: ${event}`);
            gebotState.errormessage = `ERROR: Stomp-Error: ${event}`;
        };
        stompclient.onStompError = (frame) => {
            console.log(`ERROR: Stomp-Error: ${frame}`);
            gebotState.errormessage = `ERROR: Stomp-Error: ${frame}`;
        };
        stompclient.onConnect = (frame) => {
            console.log("Connected Stompbroker to Gebote");
            stompclient.subscribe(DEST, (message) => {
                console.log(`Gebot Stompbroker received message: \n${message.body}`);
                const gebotResponseDTO: IGetGebotResponseDTO = JSON.parse(message.body);
                console.log(`Stompbroker received message: \n${gebotResponseDTO}`);
                processGebotDTO(gebotResponseDTO);
            });
            gebotState.receivingMessages = true;
        }

        stompclient.onDisconnect = () => {
            console.log("Disconnected Stompbroker from Gebote");
            gebotState.receivingMessages = false;
        }

        stompclient.activate();
    }


    async function updateGebote() {
        /*
         * holt per fetch() auf Endpunkt /api/gebot die Liste aller Gebote ab
         * (Array vom Interface-Typ IGetGebotResponseDTO, s.o.)
         * und filtert diejenigen für das Angebot mit Angebot-ID 'angebotid' 
         * (Parameter der useGebot()-Funktion, s.o.) heraus. 
         * Falls erfolgreich, wird 
         *   - das Messaging angestoßen (receiveGebotMessages(), s.o.), 
         *     sofern es noch nicht läuft
         *   - das bisherige maximale Gebot aus der empfangenen Liste gesucht, um
         *     die State-Properties 'topgebot' und 'topbieter' zu initialisieren
         *   - 'errormessage' auf den Leerstring gesetzt
         * Bei Fehler wird im State-Objekt die 'gebotliste' auf das leere Array 
         * und 'errormessage' auf die Fehlermeldung geschrieben.
         */
        try {
            const controller = new AbortController();
            const url = `/api/gebot`;

            const id = setTimeout(() => controller.abort(), 8000);

            const response = await fetch(url, {
                headers: {
                    'Authorization': 'Bearer ' + logindata.jwtToken
                },
                signal: controller.signal
            });

            clearTimeout(id);
            if(!response.ok) {
                console.log('couldnt fetch!');
                throw new Error(response.statusText);
            }

            const jsondata: Array<IGetGebotResponseDTO> = await response.json();
            const arrOfAngeboteOfId = jsondata.filter(ele => ele.angebotid === angebotid);
            gebotState.gebotliste = arrOfAngeboteOfId
            if(!gebotState.receivingMessages) {
                receiveGebotMessages();
            }
            const maxBid = Math.max(...arrOfAngeboteOfId.map(ele => ele.betrag));
            const top = arrOfAngeboteOfId.filter(ele => ele.betrag === maxBid);
            if(top.length > 0) {
                gebotState.topbieter = top[0].gebietername;
                gebotState.topgebot = top[0].betrag;
            }
            gebotState.errormessage = "";

        } catch(reason) {
            gebotState.gebotliste = [];
            gebotState.errormessage = `ERROR: ${reason}`;
        }

    }


    // Analog Java-DTO AddGebotRequestDTO.java
    interface IAddGebotRequestDTO {
        benutzerprofilid: number,
        angebotid: number,
        betrag: number
    }

    async function sendeGebot(betrag: number) {
        /*
         * sendet per fetch() POST auf Endpunkt /api/gebot ein eigenes Gebot,
         * schickt Body-Struktur gemäß Interface IAddGebotRequestDTO als JSON,
         * erwartet ID-Wert zurück (response.text()) und loggt diesen auf die Console
         * Falls ok, wird 'errormessage' im State auf leer gesetzt,
         * bei Fehler auf die Fehlermeldung
         */
        try {
            const controller = new AbortController();
            const url = '/api/gebot';
            
            const id = setTimeout(() => controller.abort(), 8000);

            const data: IAddGebotRequestDTO = {
                benutzerprofilid: logindata.benutzerprofilid,
                angebotid: angebotid,
                betrag: betrag
            }
    
            const response = await fetch(url, {
               method: 'POST',
               headers: {
                'Authorization': 'Bearer ' + logindata.jwtToken,
                'Content-Type': 'application/json'
               },
               signal: controller.signal,
               body: JSON.stringify(data) 
            })
            
            clearTimeout(id);

            console.log(response.text());
            if(response.ok) {
                gebotState.errormessage = ""
            } else {
                gebotState.errormessage = response.statusText
            }
        } catch(reason) {
            gebotState.errormessage = `ERROR: ${reason}`;
        }
    }

    // Composition Function -> gibt nur die nach außen freigegebenen Features des Moduls raus
    return {
        gebote: readonly(gebotState),
        updateGebote,
        sendeGebot
    }
}

