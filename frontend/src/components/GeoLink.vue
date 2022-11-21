<template>
<div class="geo-link">
    <a target="_blank" :href="link">
        <slot>Ortsname</slot>
    </a>
</div>
</template>


<script setup lang="ts">

const props = withDefaults(
    defineProps<{
        lon: number,
        lat: number,
        zoom: number
    }>(), {zoom: 17}
)

const link = `https://www.openstreetmap.org/#map=${props.zoom}/${props.lat}/${props.lon}`

</script>


<style scoped>

</style>