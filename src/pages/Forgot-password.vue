<template>
  <Layout class="justify-center">
    <div v-show="finished" class="rounded my-12 px-8 py-4 bg-glitter-light border-2 border-glitter-dark mx-auto">
      <p class="text-center text-xl"> {{ response_h1 }}</p> 
      <p class="text-center"> {{ response }}</p> 
    </div>
    <div class="flex container center" :class="computeBlur()"
    v-show="!finished">
  <div class="flex container center">
    <div class="rounded px-4 py-2 mx-auto bg-glitter-light border-2 border-glitter-dark">
      <p class="px-4 py-2 text-center text-violet h1">Lost password ☠️ </p>
      <p class="px-4 py-2 text-center text-violet"> Please enter your email
      below! 👨‍💻 </p>
      <div class="container">
        <div class="block py-2">
          <label class="px-1 text-violet">Email:</label>
          <input id="email" class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="email" type="email" placeholder="jane@example.com">
        </div>
          <div class="block py-2 hidden">
            <label class="px-1 text-violet">Website:</label>
            <input class="bg-white focus:outline-none focus:shadow-outline border
            border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
            leading-normal" v-model="website" type="text">
          </div>
          <div class="block py-2 hidden">
            <label class="px-1 text-violet">First name:</label>
            <input class="bg-white focus:outline-none focus:shadow-outline border
            border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
            leading-normal" v-model="firstname" type="text">
          </div>
          <div class="block py-2 hidden">
            <label class="px-1 text-violet">Last name:</label>
            <input class="bg-white focus:outline-none focus:shadow-outline border
            border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
            leading-normal" v-model="lastname" type="text" >
          </div>
      </div>
      <div class="container text-center">
        <button id="forgot-password" class="bg-ocean_green-light hover:bg-ocean_green-dark text-white font-bold py-2
        px-4 rounded my-2 mx-2" v-on:click="forgotPwd()"
        v-on:keyup.enter="forgotPwd()">Send recovery link! 📧 </button>
      </div>
      <div id="feedback" class="bg-red-100 border border-red-400 text-red-700
        px-4 py-3 rounded relative" role="alert" v-show="!positive_feedback">
        <span class="block sm:inline">{{ feedback }}</span>
      </div>
    </div>
    </div>
  </div>
  </Layout>
</template>
<script>
import handler from '../lib/responseHandler.js';

export default {
  metaInfo: {
    title: 'Welcome!'
  },
  data () {
    return {
      email: "",
      website: "",
      firstname: "",
      lastname: "",
      feedback: "",
      positive_feedback: true,
      finished: false
      }
  },
  methods: {
    validFields: function () {
      return this.lastname.length === 0 && this.website.length === 0 &&
        this.firstname.length === 0;
    },
    computeBlur: function () {
      if (this.finished) {
        return 'blur4';
      } else {
        return 'blur';
      }
    },
    forgotPwd: function () {
      if (!this.validFields()){
        console.log("invalid fields");
        return;
      }
      this.$store.dispatch('forgot_password', this.email).then((data) => {
        this.finished = true;
        this.response_h1 = "The receovery link is on it's way! 🎉"
        this.response = "A link to recovery your account has been emailed to "
          + this.email;
      }).catch(err => {
        this.positive_feedback = false;  
        this.feedback = handler.handleError(err, 
          "Invalid email!");
      });
    }
  }
}
</script>

<style>
.home-links a {
  margin-right: 1rem;
}

.blur4 {
  filter: blur(4px);
}
.blur {
  filter: blur(0);
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




