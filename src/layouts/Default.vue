<template>
  <div class="container mx-auto rounded-lg shadow-xl">
    <div class="container my-4 p-6" >
      <header class="header flex-shrink-0">
        <icon style="height:150px" v-on:click="toHome()"/>
        <nav class="nav">
          <g-link class="ml-2 text-violet" to="/">Home</g-link>
          <a v-if="loggedIn" class="ml-2 cursor-pointer text-violet" id="logout" v-on:click="logout()">Logout</a>
          <a v-if="loggedIn" class="ml-2 cursor-pointer text-violet" id="habits" v-on:click="toHabits()">Habits</a>
          <g-link class="ml-2 text-violet" to="/about/">About</g-link>
        </nav>
      </header>
      <slot class=""/>
    </div>
    <div class="pt-16 ">
      <footer class="w-full text-center border-t border-grey p-2 pin-b bg-glitter-light">
        <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div> 
      </footer>
    </div>
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
import icon from '~/assets/svgs/default.svg'
/* <--g-image src="~/default.svg" alt="Rutta" width="100"/>*/
export default {
  metaInfo: {
    title: 'Welcome!'
  }, components: {
     icon 
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
    },
    toHome: function () {
      if (this.$router.history.current.path !== '/'){
        console.log("visiting home");
        this.$router.push('/')
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

card {
  background-color: #1D4934;
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
