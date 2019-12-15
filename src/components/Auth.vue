<template>
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
          leading-normal" v-model="password" type="password"
          placeholder="************" v-on:keyup.enter="performAction()">
        </div>
      </div>
      <div class="container text-center">
        <button class="bg-green-500 hover:bg-blue-700 text-white font-bold py-2
        px-4 rounded my-2 mx-2" v-on:click="login()"
                v-on:keyup.enter="login()">{{ buttonText }}</button>
      </div>
    </div>
</template>

<script>
export default {
  metaInfo: {
    title: 'Welcome!'
  },
  data () {
    return { // overwritten later
      email: "",
      password: "",
      buttonText: "" 
    }
  },
  methods: {
    performAction: function () {} // overwritten
  }
}
</script>
