<template>
  <Layout>
  <div>
    <div class="flex">
      <div class="rounded-lg block w-1/2">
        <div class="bg-violet py-16 ">
          <users1 class="mx-16"/>
        </div>
        <div class="shadow-md px-2 py-2 bg-glitter-light">
          <div class="block">
            <div class="w-full border-b border-glitter-dark">
              <label class="text-violet">Personal information:</label>
            </div>
            <div class="flex">
              <p class="content-start px-1 text-violet">Username:</p>
              <div class="w-full">
                <p class="text-right px-1 text-violet">{{ username }}</p>
              </div>
            </div>
            <div class="flex w-full">
              <p class="content-start px-1 text-violet">Email:</p>
              <div class="w-full">
                <p class="text-right px-1 text-violet">{{ email }}</p>
              </div>
            </div>
            <div class="w-full pt-4 border-b border-glitter-dark">
              <label class="text-violet">Your stats</label>
            </div>
            <div class="flex">
              <p class="w-full text-left px-1 text-violet">Longest streak</p>
              <div class="w-full">
                <p class="text-right px-1 text-violet">{{ streak }}</p> 
              </div>
            </div>
            <div class="flex">
              <p class="w-full text-left px-1 text-violet">Habits</p>
              <div class="w-full">
                <p class="text-right px-1 text-violet">{{ habitNum }}</p> 
              </div>
            </div>
            <div class="flex">
              <p class="w-full text-left px-1 text-violet">Most loved habit</p>
              <div class="w-full">
                <p class="text-right px-1 text-violet">{{ favorite }}</p> 
              </div>
            </div>
            <div class="flex">
              <p class="w-full text-left px-1 text-violet">Teams</p>
              <div class="w-full">
                <p class="text-right px-1 text-violet">{{ teamNum }}</p> 
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="container block">
        <p class="px-4 text-xl">Habits</p>
        <div class="m-2 p-2">
        <div class="w-full py-2 flex" v-for="habit in habits">
          <div class="shadow p-2 w-full">
          <div> {{ habit.name }}</div>
          <div> {{ habit.streak }}</div>
          <div> {{ habit.team }}</div>
          </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </Layout>
</template>
<script>
import users from '~/assets/svgs/users.svg'
import users1 from '~/assets/svgs/users-1.svg'
export default {
  metaInfo: {
    title: 'Welcome!'
  },
  components: {
    users,
    users1
  },
  data () {
    return {
      username: "",
      email: "",
      feedback: "",
      streak: 0 +" 🔥",
      favorite: "",
      habitNum: 0,
      teamNum: 0,
      positive_feedback: true,
      habits: [],
    }
  },
  methods: {
    getProfile: function () {

    //this.$http
    //  .get('/api/user/' + id)
    //    .then(response => {
    //      this.habits = response.data;
    //    }).catch((error) => {console.log(error.response)});
    }
  },
  mounted () {
    this.getProfile();
  }
}
</script>
