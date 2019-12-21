<template>
  <Layout>
  <div class="flex container center">
    <div class="rounded px-4 py-2 mx-auto bg-glitter-light border-2 border-glitter-dark">
      <p class="px-4 py-2 text-center text-violet"> Welcome back!👏 <br/>Please enter your credentials below </p>
      <div class="container">
        <div class="block py-2">
          <label class="px-1 text-violet">Email:</label>
          <input class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="email" type="email" placeholder="jane@example.com">
        </div>
        <div class="block py-2">
          <label class="px-1 text-violet">Password:</label>
          <input class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="password" type="password" placeholder="************" v-on:keyup.enter="login()">
        </div>
      </div>
      <div class="container text-center">
        <button class="bg-ocean_green-light hover:bg-ocean_green-dark text-white font-bold py-2
        px-4 rounded my-2 mx-2" v-on:click="login()" v-on:keyup.enter="login()">Login</button>
      </div>
      <p class="text-center text-xs text-violet" v-on:click="forgotPwd()">Forgot password?</p> 
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
      email: "",
      password: ""
      }
  },
  methods: {
    forgotPwd: function () {
      alert('Cannot help you there bud!');
    },
    login: function () {
      let email = this.email;
      let pwd = this.password;
      const usernamePasswordBuffer = Buffer.from(email+ ':' + pwd);
      const base64data = usernamePasswordBuffer.toString('base64');
      this.$store.dispatch('login', base64data).then(() =>
        this.$router.push('/habits'))
       .catch(err => console.log(err));
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




