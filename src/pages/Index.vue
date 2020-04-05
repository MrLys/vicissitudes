<template>
  <Layout>
  <div class="block">
    <div class="container px-auto">
    <div class="block container px-4 bg-glitter-light rounded-lg shadow-xl
      my-12 py-6">
      <p class="py-2"> Exercise </p>
      <div class="flex container">
        <div class="py-2 w-1/6 block border-r-2 last:border-r-0 text-center
          bg-gray-100" v-for="(day,index) in week">
            <div v-show="!xs">
            {{ day.day }} 
            </div>
            <div v-show="xs">
            {{ day.day_xs }} 
            </div>
        </div>
      </div>

    <div class="flex container">
      <div class="w-1/6 block border-r last:border-r-0" v-for="day in
        week">
        <div :class="computedClass(day)">
        </div>
      </div>
      </div>
    </div>
    </div>
    <div class="mx-auto text-center">
      <p class="h1"> Welcome to Rutta! </p>
      <p> This is a free and simple habit tracker. </p>
    </div>
      <div class="my-4 flex mx-auto" v-show="!loggedIn">
        <button class="bg-ocean_green-light hover:bg-ocean_green-dark
        text-white font-bold py-2 x-4 rounded my-2 mx-2 mx-auto w-1/3" v-on:click="toLogin()">
          <p class="px-2">Log in</p>
        </button>
        <button class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
        x-4 rounded my-2 mx-2 mx-auto w-1/3" v-on:click="toRegister()">
          <p class="px-2">Register</p>
        </button>
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
    return {
      loggedIn: this.$store.getters.isLoggedIn,
      grooves: {
        'default': 'py-4 border-2 ',
        'none': 'bg-gray-200 border-gray-200 ',
        'selected': 'border-blue-500 ', 
        'success': 'bg-green-500 border-green-500 ',
        'fail': 'bg-red-500 border-red-500 ',
        'pass': 'stripes border-white '},
      week: [
        {day_xs:'Mon', day:'Monday', style: "py-4 border-2 bg-ocean_green-dark border-ocean_green-dark"},
        {day_xs:'Tue', day:'Tuesday', style: 'py-4 border-2 bg-gray-200 border-gray-200 '}, 
        {day_xs:'Wed', day:'Wednesday', style: "py-4 border-2 bg-red-500 border-red-500 "},
        {day_xs:'Thu', day:'Thursday', style: "py-4 border-2 bg-ocean_green-dark border-ocean_green-dark"}, 
        {day_xs:'Fri', day:'Friday', style: "py-4 border-2 stripes border-gray-200"}, 
        {day_xs:'Sat', day:'Saturday', style: "py-4 border-2 bg-ocean_green-dark border-ocean_green-dark"},
        {day_xs:'Sun', day:'Sunday', style: "py-4 border-2 bg-ocean_green-dark border-ocean_green-dark"}],
      xs: typeof window !== 'undefined' && window && window.innerWidth < 960,
    } 
  },
  methods: {
    computedClass: function (item) {
      return item.style;
    },
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
  },
  mounted () {
    console.log("Finally mounted");
    window.addEventListener("resize", () => {
      console.log("rezising");
      if (typeof window !== 'undefined' && window) {
        this.xs = window.innerWidth < 960;
      }
    });
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
    transparent 3px,
    #ccc 2px,
    #ccc 4px
  )
}
</style>
