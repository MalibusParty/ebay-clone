import type { IAngebotListeItem } from "@/services/IAngebotListeItem";
import type { IBackendInfoMessage } from "./IBackendInfoMessage";
import { reactive, readonly } from "vue";

import { Client } from '@stomp/stompjs';
import { stringifyQuery } from "vue-router";
const wsurl = `ws://${window.location.host}/stompbroker`

interface ILoginState {
    username: string,
    name: string,
    benutzerprofilid: number,
    loggedin: boolean,
    jwtToken: string
    errormessage: string
}

/* reaktives Objekt zu Interface ILoginState */
const loginState = reactive<ILoginState>({
    username: "",
    name: "",
    benutzerprofilid: 0,
    loggedin: false,
    jwtToken: "",
    errormessage: ""
});

// Analog Java-Records auf Serverseite:
// public record JwtLoginResponseDTO(String username, String name, Long benutzerprofilid, String jwtToken) {};
interface IJwtLoginResponseDTO {
    username: string,
    name: string,
    benutzerprofilid: number,
    jwtToken: string
}

// public record JwtLoginRequestDTO(String username, String password) {};
interface IJwtLoginRequestDTO {
    username: string,
    password: string
}


async function login(username: string, password: string) {
   /*
    * sendet per fetch() POST auf Endpunkt /api/login ein IAddGebotRequestDTO als JSON
    * erwartet IJwtLoginResponseDTD-Struktur zurück (JSON)
    * 
    * Falls ok, wird 'errormessage' im State auf leer gesetzt,
    * die loginState-Eigenschaften aus der Antwort befüllt
    * und 'loggedin' auf true gesetzt
    * 
    * Falls Fehler, wird ein logout() ausgeführt und auf die Fehlermeldung in 'errormessage' geschrieben
    */

   try {
    const controller = new AbortController();
    const url = '/api/login';

    const id = setTimeout(() => controller.abort(), 8000);

    const data: IJwtLoginRequestDTO = {
        username: username,
        password: password
    }

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        signal: controller.signal,
        body: JSON.stringify(data)
    });

    clearTimeout(id);
    //console.log(response.text());
    
    if(response.ok) {
        loginState.errormessage = "";
        const jsondata: IJwtLoginResponseDTO = await response.json();
        loginState.benutzerprofilid = jsondata.benutzerprofilid;
        loginState.username = jsondata.username;
        loginState.name = jsondata.name;
        loginState.jwtToken = jsondata.jwtToken;
        loginState.loggedin = true;

    } else {
        loginState.errormessage = response.statusText;
        logout();
    }
   } catch(reason) {
        loginState.errormessage = `ERROR: ${reason}`;
        logout();
   }

}

function logout() {
    console.log(`logout(${loginState.name} [${loginState.username}])`)
    loginState.loggedin = false
    loginState.jwtToken = ""
    loginState.benutzerprofilid = 0
    loginState.name = ""
    loginState.username = ""
}


export function useLogin() {
    return {
        logindata: readonly(loginState),
        login,
        logout,
    }
}

