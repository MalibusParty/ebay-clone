<template>
<div>
    <table>
        <thead>
            <th id="head-one"><RouterLink :to="{ path: '/gebot/' + angebot.angebotid }">{{angebot.beschreibung}}</RouterLink></th>
            <th id="head-two">{{angebot.gebote}} Gebote</th>
            <th id="head-three">{{angebot.topgebot}}€</th>
            <th id="head-four"><button class="klapp-btn" @click="klappen()" href="">{{btnStyle}}</button></th>
        </thead>
        <tbody v-if="state.aufgeklappt">
            <tr>
                <td>Letztes Gebot</td>
                <td>{{angebot.topgebot}} EUR (Mindestpreis war {{angebot.mindestpreis}} EUR)</td>
            </tr>
            <tr>
                <td>Abholort</td>
                <td>{{angebot.abholort}}</td>
            </tr>
            <tr>
                <td>bei</td>
                <td>{{angebot.anbietername}}</td>
            </tr>
            <tr>
                <td>bis</td>
                <td>{{angebot.ablaufzeitpunkt}}</td>
            </tr>
        </tbody>
    </table>
</div>
</template>


<script setup lang="ts">
import type { AngebotListeDing } from '@/services/IAngebotListeItem';
import { computed } from '@vue/reactivity';
import { reactive, ref } from 'vue';


const props = defineProps<{
    angebot: AngebotListeDing
}>();

const state = reactive({
    aufgeklappt: false
});

function klappen() {
    state.aufgeklappt = !state.aufgeklappt
}

const btnStyle = computed(() => state.aufgeklappt ? '^' : 'v')
const gebotLink = ref();
gebotLink.value = `/gebot/${props.angebot.angebotid}`;
</script>

<style scoped>

table {
    width: 100%;
    margin-bottom: 2.5%;
}

thead {
    width: 100%;
    padding-top: 1.5%;
    padding-bottom: 1.5%;
}

#head-one {
    width: 30%;
} 

#head-two {
    width: 40%;
}

#head-three {
    width: 10%;
}

#head-four {
    width: 20%;
}

#head-four button {
    width: 100%;
    background-color: rgb(0, 102, 255);
    border: none;
    color: white;
    padding: 1.5%;
}

#head-four button:hover {
    background-color: rgba(0, 51, 204, 255);
}
</style>