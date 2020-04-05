<template>
  <Layout>
  <div class="container mt-16 text-center">
    <div class="rounded px-8 py-4 bg-glitter-light border-2">
      <p v-show="loading">Please wait while we activate your account! </p>
      <p v-show="error"> Something has gone amiss 😬 </p>
  </div>
  </div>
  </Layout>
</template>
<script>
export default {
  data () {
    return {
      loading: true,
      error: false,
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
            setTimeout(() => this.$router.push(url+'/login'), 3000);
          }).catch((error) => 
            {console.log(error.response); 
              this.loading = false;
              this.error = true;
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
