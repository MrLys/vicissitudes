<template>
  <Layout>
    <div class="flex container center">
      <div>Welcome!</div>
      <div class="my-4 block center mx-auto">
        <div>
        <button class="bg-green-500 hover:bg-blue-700 text-white font-bold py-2
        x-4 rounded my-2 mx-2 mx-auto" v-on:click="toLogin()">
          <p class="px-2" v-if="!loggedIn">Log in</p>
        </button>
        </div>
        <div>
        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2
        x-4 rounded my-2 mx-2 mx-auto" v-on:click="toRegister()">
          <p class="px-2" v-if="!loggedIn">Register</p>
        </button>
        </div>
      </div>
    </div>
  </Layout>
</template>
<script>
export default {
  metaInfo: {
    title: 'Welcome!'
  },
  data () {
    return {loggedIn: this.$store.getters.isLoggedIn} 
  },
  methods: {
    toLogin: function () {
      if(this.$store.getters.isLoggedIn) {
        this.$router.push('/habits');  
      } else {
        this.$router.push('/login');
      }
    },
    toRegister: function () {
      this.$store.dispatch('logout').then(() => {
        this.$router.push('/register')
      });
    }
  }
}
</script>

<style>
.home-links a {
  margin-right: 1rem;
}

.stripes {
   background: repeating-linear-gradient(
    45deg,
    transparent,
    transparent 2px,
    #ccc 2px,
    #ccc 4px
  )
}
</style>
