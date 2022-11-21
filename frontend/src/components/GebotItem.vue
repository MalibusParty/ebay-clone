<template>
<div id="gebot-item-flex">
    <div class="gebot-item1">{{zeitpunkt}}</div>
    <div class="gebot-item2">{{name}}</div>
    <div class="gebot-item2">{{betrag}}</div>
</div>
</template>

<script setup lang="ts">


const props = defineProps<{
    zeitpunkt: string,
    name: string,
    betrag: number
}>();

</script>

<style scoped>

#gebot-item-flex {
    display: flex;
    flex-direction: row;
    border-bottom: 1px solid black;
    width: 100%;
}

.gebot-item1 {
    padding: 1.5%;
    width: 50%;
}

.gebot-item2 {
    padding: 1.5%;
    width: 25%;
}
</style>