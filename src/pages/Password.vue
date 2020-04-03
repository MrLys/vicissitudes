<template>
  <Layout class="justify-center">
    <div v-if="finished" class="rounded my-12 px-8 py-4 bg-glitter-light border-2 border-glitter-dark mx-auto">
      <p class="text-center text-xl"> {{ response_h1 }}</p> 
      <p class="text-center"> {{ response }}</p> 
    </div>
    <div class="flex container center" :class="computeBlur"
    v-if="!finished">
      <div class="rounded px-8 py-4 bg-glitter-light border-2 border-glitter-dark">
        <p class="px-4 py-2 text-center text-violet"> Please enter your new credentials below </p>
        <div class="container">
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
            <input id="password1" class="bg-white focus:outline-none focus:shadow-outline border
            border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
            leading-normal" v-model="password" type="password" placeholder="************" v-on:keyup.enter="register()">
          </div>
          <div class="block py-2">
            <label class="px-1 text-violet">Repeat password:</label>
            <input id="password2" class="bg-white focus:outline-none focus:shadow-outline border
            border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
            leading-normal" v-model="password2" type="password" placeholder="************" v-on:keyup.enter="register()">
          </div>
        </div>
        <div class="container text-center">
          <button class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
          px-4 rounded my-2 mx-2" v-on:click="register()"
                  v-on:keyup.enter="register()">Update</button>
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
export default {
  data () {
    return {
      feedback: "",
      positive_feedback: true,
      loading: true,
    }
  },
  methods: {
    activate: function () {
      let token = this.$route.query.token;
      const url = process.env.GRIDSOME_API_URL;
      console.log(token);
      if(token !== undefined) {
        let param = 'activation_token='+token;
        this.$http
          .post(url+'/api/activate?' + param)
          .then(response => {
            this.$router.push('/login');
          }).catch((error) => {console.log(error.response); this.loading = false;});
      } else {
        this.loading = false;
      }
    }
  },
  mounted () {
    this.activate();
  }
}
</script>
