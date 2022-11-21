<template>
<div id="ang-lst-comp">
    <div id="search-bar">
        <input type="text" v-model="input">
        <button @click="clear()">clear</button>
    </div>
    <ul id="lst-items">
        <li v-for="item in angeboteListe" :key="item.angebotid">
            <AngebotListeItem :angebot="item"/>
        </li>
    </ul>
</div>
</template>


<script setup lang="ts">
import { ref, computed } from 'vue';
import { useAngebot } from '@/services/useAngebot';
import AngebotListeItem from './AngebotListeItem.vue';

const { angebote, updateAngebote } = useAngebot();
const input = ref("");

const angeboteListe = computed(() => {
    return angebote.value.angebotliste.filter(a => 
        a.abholort.toLowerCase().includes(input.value.toLowerCase()) ||
        a.beschreibung.toLowerCase().includes(input.value.toLowerCase()) ||
        a.anbietername.toLowerCase().includes(input.value.toLowerCase())
    )
})



function clear() {
    input.value = ""
}
</script>


<style scoped>

#ang-lst-comp {
    width: 100%;
    display: flex;
}

#search-bar {
        border: 1px solid black;
        margin-top: 2%;
}

#search-bar input {
    width: 80%;
    padding: 1.5%;
    border: none;
}

#search-bar button {
    width: 20%;
    background-color: rgb(0, 102, 255);
    border: none;
    color: white;
    padding: 1.5%;
}

#search-bar button:hover {
    background-color: rgba(0, 51, 204, 255);
}

#lst-items {
    list-style-type: none;
    width: 100%;
    padding: 0;
    background-color: white;
}

#lst-items li {
    margin-top: 2%;
    width: 100%;
    border-bottom: 1px solid black;
}

</style>