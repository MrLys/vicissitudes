<template>
  <div class="container mx-auto p-6 bg-white rounded-lg shadow-xl">
    <header class="header flex-shrink-0">
      <strong>
        <g-link to="/">{{ $static.metadata.siteName }}</g-link>
      </strong>
      <nav class="nav">
        <g-link class="ml-2" to="/">Home</g-link>
        <a v-if="loggedIn" class="ml-2 cursor-pointer" v-on:click="logout()">Logout</a>
        <a v-if="loggedIn" class="ml-2 cursor-pointer" v-on:click="toHabits()">Habits</a>
        <g-link class="ml-2" to="/about/">About</g-link>
      </nav>
    </header>
    <slot class="container mx-auto"/>
  </div>
</template>

<static-query>
query {
  metadata {
    siteName
  }
}
</static-query>
<script>
export default {
  metaInfo: {
    title: 'Welcome!'
  },
  data () {
    return {loggedIn: this.$store.getters.isLoggedIn}
  },
  methods: {
    logout: function () {
      console.log("logging out");
      this.$store.dispatch('logout').then(() => {
        this.$router.push('/login')
      });
    },
    toHabits: function () {
      if (this.$router.history.current.path !== '/habits'){
        console.log("visiting habits");
        this.$router.push('/habits')
      }
    }
  }
}
</script>
<style>
body {
  font-family: -apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif;
  margin:0;
  padding:0;
  line-height: 1.5;
}


.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  height: 80px;
}

.nav__link {
  margin-left: 20px;
}
</style>
