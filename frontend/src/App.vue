<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router';
import GeoLink from './components/GeoLink.vue'
import AngebotListe from './components/AngebotListe.vue'
import { useFakeAngebot } from '@/services/useFakeAngebot';
import AngebotListeItem from './components/AngebotListeItem.vue';
import { useLogin } from './services/useLogin';

const { angebote, bietenSimulieren } = useFakeAngebot();
const { logindata } = useLogin();

</script>


<template>
<div id="app-body">
  <nav class="nav-bar">
    <RouterLink class="nav-btn" to="/">Angebote</RouterLink>
    <RouterLink class="nav-btn" id="login-btn" to="/login">Logout</RouterLink>
  </nav>
  <div id="header-box">
    <h1 id="ang-header">Treffliche Angebote</h1>
    <p id="user-header" v-if="!logindata.loggedin">Sie sind noch nicht eingeloggt</p>
    <p id="user-header" v-if="logindata.loggedin">Sie sind {{logindata.username}}</p>
  </div>
  <!-- <GeoLink :lon="52.9849" :lat="9.5794" :zoom="10">Visselhövede</GeoLink>
  <button style="margin: 0px 0px 10px 0px" type="button" @click="bietenSimulieren()" >Bieten simmulieren</button>
  <AngebotListeItem :angebot="angebote[1]" />
  <AngebotListe/> -->

  <RouterView />
</div>
</template>


<style>
* {
  box-sizing: border-box;
  font-size: 24pt;
  font-family: Arial, Helvetica, sans-serif;
}

body {
  margin: 0;
}

#app-body {
  margin: auto;
  width: 60%;
}

.nav-bar {
  display: flex;
  height: 10%;
  background-color: rgb(0, 102, 255);
}

.nav-btn {
  text-decoration: none;
  color: white;
  padding: 1.5%;
  border-right: 1px solid rgb(194, 194, 194);
}

.nav-btn:hover {
  background-color: rgba(0, 51, 204, 255);
}

#login-btn {
  justify-self: end;
}

#header-box {
  display: flex;
  margin-top: 2%;
  width: 100%;
  flex-direction: column;
  background-color: rgb(0, 102, 255);
  color: white;
}
</style>
