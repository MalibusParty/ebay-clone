<template>
    <div id="gebot-box">
        <div id="header-box">
            <p id="errormessage" v-if="gebote.errormessage !== ''">{{gebote.errormessage}}</p>
        </div>
        <div id="versteig-item">
            <div id="versteig-item-titel">Versteigerung {{passendesAngebot?.beschreibung}} ab {{passendesAngebot?.mindestpreis}}EUR</div>
            <p id="versteig-user" v-if="passendesAngebot">Von {{passendesAngebot?.anbietername}}, abholbar in <GeoLink :lon="passendesAngebot.lon" :lat="passendesAngebot.lat" :zoom="10">{{passendesAngebot.abholort}}</GeoLink> bis {{passendesAngebot?.ablaufzeitpunkt}}</p>
        </div>
        <div id="beat-me">
            <p id="buy-time">Bisheriges Topgebot von {{gebote.topgebot}}EUR ist von {{gebote.topbieter}}, Restzeit {{restzeit}} Sekunden</p>
            <div id="gebot-input-box" v-if="restzeit > 0">
                <input id="gebot-input" type="number" v-model="betrag">
                <button id="gebot-btn" @click="bieteFürAngebot()">bieten</button>
            </div>
            <ul id="gebot-lst">
                <li class="gebot-item" v-for="item in gebotListe" :key="item.gebotid">
                    <GebotItem :zeitpunkt="item.gebotzeitpunkt" :name="item.gebietername" :betrag="item.betrag" />
                </li>
            </ul>
        </div>
    </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue';
import { useAngebot } from '@/services/useAngebot';
import GeoLink from '../components/GeoLink.vue';
import { useGebot } from '@/services/useGebot';
import { computed } from '@vue/reactivity';
import GebotItem from '../components/GebotItem.vue';

const { angebote } = useAngebot();

const props = defineProps<{
    angebotidstr: string
}>();

const { gebote, updateGebote, sendeGebot } = useGebot(parseInt(props.angebotidstr));

const passendesAngebot = ref(angebote.value.angebotliste.find(ele => ele.angebotid === parseInt(props.angebotidstr)));
const betrag = ref("");
const restzeit = ref(0);

onMounted(() => {
    updateGebote();
});

let gebotListe = computed(() => {
    return gebote.gebotliste.slice().sort((a, b) => b.betrag - a.betrag).slice(0, 9);
});

function updateRestzeit() {
    let ablauf = passendesAngebot.value ? passendesAngebot.value.ablaufzeitpunkt : "";
    restzeit.value = ((new Date(ablauf).getTime() - Date.now()) / 1000) | 0;
    if(restzeit.value <= 0) {
        clearInterval(timerid);
    }
}

let timerid = setInterval(updateRestzeit, 1000);
onBeforeUnmount(() => {
    clearInterval(timerid);
})

function bieteFürAngebot() {
    const topGeb = passendesAngebot.value? passendesAngebot.value.topgebot : 0;
    console.log(topGeb)
    const bietenderBetrag = parseInt(betrag.value);
    if(bietenderBetrag > topGeb) {
        sendeGebot(bietenderBetrag);
    }
}

</script>

<style scoped>

#gebot-box {
    display: flex;
    width: 100%;
    flex-direction: column;
}

#header-box {
    display: flex;
    margin-top: 2%;
    width: 100%;
    flex-direction: column;
    background-color: rgb(0, 102, 255);
    color: white;
}

#link-box {
    display: flex;
    width: 100%;
    justify-content: flex-end;
}

#header-box a {
    text-decoration: none;
    color: white;
    padding: 1.5%;
}

#header-box a:hover {
    background-color: rgba(0, 51, 204, 255);
}

#errormessage {
    background-color: rgb(227, 42, 0);
    color: white;

    margin-top: 2%;
    padding: 1.5%;
}

#gebot-input {
    width: 80%;
    padding: 1.5%;
    border-right: none;
}

#gebot-btn {
    width: 20%;
    padding: 1.5%;
    background-color: rgb(0, 102, 255);
    color: white;
}

#gebot-btn:hover {
    background-color: rgba(0, 51, 204, 255);
}

#gebot-lst {
    list-style-type: none;
    width: 100%;
    padding: 0;
    background-color: white;
}

#gebot-item {
    display: flex;
    margin-top: 2%;
    width: 100%;
    border-bottom: 1px solid black;
    flex-direction: row;
}




</style>