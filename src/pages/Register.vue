<template>
  <Layout>
  <div class="flex container center">
    <div class="rounded px-8 py-4 bg-glitter-light border-2 border-glitter-dark">
      <p class="px-4 py-2 text-center text-violet"> Please enter your credentials below </p>
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
          leading-normal" v-model="password" type="password" placeholder="************" v-on:keyup.enter="register()">
        </div>
        <div class="block py-2">
          <label class="px-1 text-violet">Repeat password:</label>
          <input class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="password2" type="password" placeholder="************" v-on:keyup.enter="register()">
        </div>
      </div>
      <div class="container text-center">
        <button class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
        px-4 rounded my-2 mx-2" v-on:click="register()"
                v-on:keyup.enter="register()">Register</button>
      </div>
      <div id="feedback" class="bg-red-100 border border-red-400 text-red-700
        px-4 py-3 rounded relative" role="alert" v-if="!positive_feedback">
        <span class="block sm:inline">{{ feedback }}</span>
        <span class="absolute top-0 bottom-0 right-0 px-4 py-3">
          <svg class="fill-current h-6 w-6 text-red-500" role="button" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><title>Close</title><path d="M14.348 14.849a1.2 1.2 0 0 1-1.697 0L10 11.819l-2.651 3.029a1.2 1.2 0 1 1-1.697-1.697l2.758-3.15-2.759-3.152a1.2 1.2 0 1 1 1.697-1.697L10 8.183l2.651-3.031a1.2 1.2 0 1 1 1.697 1.697l-2.758 3.152 2.758 3.15a1.2 1.2 0 0 1 0 1.698z"/></svg>
        </span>
      </div>
    </div>
    <div class="container block mx-auto">
<div class="mx-4 px-8 py-3" role="alert">
      <p class="px-4 py-2 my-2 text-center text-white"> Please enter your credentials below </p>
</div>
<div class="mx-4 my-4 bg-blue-100 border-t border-b border-blue-500 text-blue-700 px-4 py-3" role="alert">
  <p class="font-bold">Email</p>
  <p class="text-sm">This is only stored to give you the ability to recover
  your account if it is lost. We will never use it for advertisement 🎉</p>
</div>
<div class="mx-4 my-10 bg-blue-100 border-t border-b border-blue-500 text-blue-700 px-4 py-3" role="alert">
  <p class="font-bold">Passwords</p>
  <p class="text-sm">We take your security seriously! To help us in this
  endeavour, please choose a
  unique strong password! 🤓</p>
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
    return {
      email: "",
      password: "",
      password2: "",
      feedback: "",
      positive_feedback: true
    }
  },
  methods: {
    computeFeedbackClass: function () {
      return (this.positive_feedback) ? 'text-white' : 'text-red-500';
    },
    register: function () {
      // register account
      let email = this.email;
      let pwd = this.password;
      let pwd2 = this.password2;
      if (pwd !== pwd2) {
        this.positive_feedback = false;
        this.feedback = "The two passwords you entered are not equal";
        return;
      }

      if (pwd.length < 8) {
        this.positive_feedback = false;
        this.feedback = "The password must contain at least 8 characters";
        return;
      }
      this.$store.dispatch('register', {username: email, email: email,
        password: pwd}).then(() =>
          this.$router.push('/waiting'))
          .catch(err => {this.positive_feedback = false;  this.feedback = err});
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




