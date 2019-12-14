<template>
  <Layout>
  <div class="flex container center">
    <div class="rounded px-4 py-2 mx-auto bg-blue-200 border-2
    border-blue-400 ">
      <p class="px-4 py-2 text-center"> Please enter your credentials below </p>
      <div class="container">
        <div class="block py-2">
          <label class="px-1">Email:</label>
          <input class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="email" type="email" placeholder="jane@example.com">
        </div>
        <div class="block py-2">
          <label class="px-1">Password:</label>
          <input class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="password" type="password" placeholder="************" v-on:keyup.enter="register()">
        </div>
        <div class="block py-2">
          <label class="px-1">Repeat password:</label>
          <input class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="password2" type="password" placeholder="************" v-on:keyup.enter="register()">
        </div>
      </div>
      <div class="container text-center">
        <button class="bg-green-500 hover:bg-blue-700 text-white font-bold py-2
        px-4 rounded my-2 mx-2" v-on:click="register()"
                v-on:keyup.enter="register()">Register</button>
      </div>
      <p id="feedback" :class="computeFeedbackClass()">{{ feedback }}</p>
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
          this.$router.push('/login'))
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




