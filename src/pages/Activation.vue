<template>
  <Layout>
  <div class="container mt-16 text-center">
    <div class="rounded px-8 py-4 bg-glitter-light border-2">
      <p v-if="loading">Please wait while we activate your account! </p>
      <p v-if="positive_feedback">Your account has been activated! 🎉 </br> You will be
      redirected to the login page soon!</p>
      <p v-if="!loading && !positive_feedback">Something went wrong with
      activating your account! 😳 </p>
  </div>
  </div>
  </Layout>
</template>
<script>
export default {
  data () {
    return {
      feedback: "",
      positive_feedback: false,
      loading: true,
    }
  },
  methods: {
    activate: function () {
      let token = this.$route.query.token;
      console.log(token);
      if(token !== undefined) {
        let param = 'activation_token='+token;
        this.$http
          .post('/api/activate?' + param)
          .then(response => {
            this.loading = false;
            this.positive_feedback = true;
            setTimeout(() => this.$router.push('/login'), 4000);
          }).catch((error) => {
            console.log(error.response); 
            this.loading = false;
            });
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
