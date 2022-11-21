<template>
    <main>
        <div id="ang-view-err" v-if="angebote.errormessage !== ''">Error: {{ angebote.errormessage }}</div>
        <div id="header-box">
            <h1 id="ang-header">Wir haben aktuell {{ angebote.angebotliste.length }} Angebote für Sie</h1>
            <button id="update-ang-btn" @click="updateAngebote()">Refresh</button>
        </div>
        <AngebotListe id="ang-liste"/>
    </main>
</template>

<script setup lang="ts">
import { useAngebot } from "@/services/useAngebot";
import { onMounted } from "vue";
import AngebotListe from "../components/AngebotListe.vue";

const { angebote, updateAngebote, receiveAngebotMesseges } = useAngebot();


onMounted(() => {
    updateAngebote();
    receiveAngebotMesseges();
});
</script>

<style scoped>

#ang-view-err {
    background-color: rgb(227, 42, 0);
    color: white;

    margin-top: 2%;
    padding: 1.5%;
}

#header-box {
    display: flex;
    margin-top: 2%;
    width: 100%;
}

#ang-header {
    width: 80%;
}

#update-ang-btn {
    width: 20%;
    background-color: rgb(0, 102, 255);
    border: none;
    color: white;
    padding: 1.5%;
}

#update-ang-btn:hover {
    background-color: rgba(0, 51, 204, 255);
}

#ang-liste {
    width: 100%;
}

</style>