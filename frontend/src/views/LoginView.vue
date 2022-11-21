<template>
    <div id="gebot-box">
        <div id="header-box">
            <p id="errormessage" v-if="logindata.errormessage !== ''">{{logindata.errormessage}}</p>
        </div>
        <h1>Bitte inloggieren Sie sich</h1>
        <input id="username-input" type="text" v-model="username">
        <input id="password-input" type="password" v-model="password">
        <button id="login-btn" @click="doLogin()">login</button>
    </div>
</template>

<script setup lang="ts">
import { useLogin } from '@/services/useLogin';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import router from '@/router';



const { logindata, login, logout } = useLogin();

const username = ref("");
const password = ref("");

onMounted(() => {
    logout();
});

async function doLogin() {
    await login(username.value, password.value);
    router.push('/');
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

#login-btn {
    background-color: rgb(0, 102, 255);
    color: white;
    padding: 1.5%;
}

#login-btn:hover {
    background-color: rgba(0, 51, 204, 255);
}

input {
    padding: 1.5%;
}
</style>