<template>
  <Layout class="justify-center">
    <div v-if="finished" class="rounded my-12 px-8 py-4 bg-glitter-light border-2 border-glitter-dark mx-auto">
      <p class="text-center text-xl"> {{ response_h1 }}</p> 
      <p class="text-center"> {{ response }}</p> 
    </div>
    <div class="flex container center" :class="computeBlur()"
    v-if="!finished">
      <div class="rounded px-8 py-4 bg-glitter-light border-2 border-glitter-dark">
        <p class="px-4 py-2 text-center text-violet"> Please enter your credentials below </p>
        <div class="container">

          <div class="block py-2">
            <label class="px-1 text-violet">Email:</label>
            <input class="bg-white focus:outline-none focus:shadow-outline border
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
          <p class="block sm:inline">{{ feedback }}</p>
          <br/>
          <p class="block sm:inline">{{ feedback_suggestion }}</p>
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
import handler from '../lib/responseHandler.js';
import passwordStrength from 'zxcvbn';

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
      feedback_suggestion: "",
      response: "",
      response_h1: "",
      website: "",
      firstname: "",
      lastname: "",
      positive_feedback: true,
      finished: false
    }
  },
  methods: {
    computeBlur: function () {
      if (this.finished) {
        return 'blur4';
      } else {
        return 'blur';
      }
    },
    computeFeedbackClass: function () {
      return (this.positive_feedback) ? 'text-white' : 'text-red-500';
    },
    validEmail: function (email) {
      return true;
    },
    validFields: function () {
      return this.lastname.length === 0 && this.website.length === 0 &&
        this.firstname.length === 0;
    },
    validPassword: function (pwd, pwd2) {
      if (pwd !== pwd2) {
        console.log("inequal pwds");
        this.positive_feedback = false;
        this.feedback = "The two passwords you entered are not equal";
        return false;
      }

      if (pwd.length < 8) {
        console.log("short pwds");
        this.positive_feedback = false;
        this.feedback = "The password must contain at least 8 characters";
        return false;
      }
      let strength_result = passwordStrength(pwd);
      if (strength_result.score < 3) {
        this.positive_feedback = false;
        console.log(strength_result.feedback.warning);
        if (strength_result.feedback.warning !== undefined) {
          this.feedback = strength_result.feedback.warning;
        } else {
          this.feedback = "Please choose a stronger password!";
        }
        if (strength_result.feedback.suggestions !== undefined) {
          this.feedback_suggestion = strength_result.feedback.suggestions[0];
        }
        return false;
      }
      return true;
    },
    register: function () {
      // register account
      let email = this.email;
      let pwd = this.password;
      let pwd2 = this.password2;
      console.log("validating");
      if (!this.validFields()){
        console.log("invalid fields");
        return;
      }
      console.log("validating1");
      if (!this.validPassword(pwd, pwd2)){
        return;
      }
      console.log("validating2");
      if (!this.validEmail(email)) {
        return;
      }
      console.log("validating3");

      this.$store.dispatch('register', {username: email, email: email,
        password: pwd}).then(() =>
          { 
            this.finished = true;
            this.response_h1 =  
              "Congratulations, you've successfully created an account! 🎉"
            this.response = "A link to activate your account has been emailed to " + email;
          })
          .catch(err => {
            this.positive_feedback = false;  
            this.feedback = handler.handleError(err, "");});
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




