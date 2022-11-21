import { readonly, ref } from "vue";
import type { IAngebotListeItem } from "./IAngebotListeItem";
import { Client, type Message } from '@stomp/stompjs';
import type { IBackendInfoMessage } from "./IBackendInfoMessage";
import { useLogin } from "./useLogin";

interface IAngebotState {
    angebotliste: IAngebotListeItem[],
    errormessage: string
}


const angebotState = ref<IAngebotState>({
    angebotliste: [],
    errormessage: ""
});

const { logindata } = useLogin();

async function updateAngebote(): Promise<void> {
    try {
        const controller = new AbortController();
        const url = `/api/angebot`;

        const id = setTimeout(() => controller.abort(), 8000);

        const response = await fetch(url, {
            headers: {
                'Authorization': 'Bearer ' + logindata.jwtToken
            },
            signal: controller.signal
        });

        clearTimeout(id);
        if(!response.ok){
            console.log('couldnt fetch!');
            throw new Error(response.statusText);
        } 

        const jsondata: Array<IAngebotListeItem> = await response.json();
        //const parsedJson = JSON.parse(jsondata);
        angebotState.value.angebotliste = jsondata;
        angebotState.value.errormessage = "";

    } catch(reason) {
        angebotState.value.errormessage = `ERROR: ${reason}`;
    }
}

export function useAngebot() {
    return {
        angebote: readonly(angebotState),
        updateAngebote,
        receiveAngebotMesseges
    }
}

function receiveAngebotMesseges() {
    const wsurl = `ws://${window.location.host}/stompbroker`;
    const DEST = "/topic/angebot";

    const stompclient = new Client({ brokerURL: wsurl })
    stompclient.onWebSocketError = (event) => console.log(`ERROR: WebSocket-Error: ${event}`);
    stompclient.onStompError = (frame) => console.log(`ERROR: Stomp-Error: ${frame}`);

    stompclient.onConnect = (frame) => {
        console.log("Connected Stompbroker to Angebote");
        stompclient.subscribe(DEST, (message) => {
            console.log(`Stompbroker received message: \n${message.body}`);
            const backendInfoMsg: IBackendInfoMessage = JSON.parse(message.body);
            console.log(`Stompbroker received message: \n${backendInfoMsg}`);
            updateAngebote();
        });
    }
    
    stompclient.onDisconnect = () => {
        console.log("Disconnected Stompbroker");
    }
    
    stompclient.activate();
    
    // try {
    //     stompclient.publish({
    //         destination: DEST,
    //         headers: {},
    //         body: JSON.stringify()})
    // } catch(error) {
    //     console.log(`ERROR: Sending error: ${error}`);
    // }
}