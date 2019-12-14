<template>
  <Layout>

    <div class="flex container">

        <div class="py-2 w-1/6 block border-r last:border-r-0 text-center bg-gray-100" v-for="day in week">
          {{ day.day }} 
        </div>
  </div>
  <div class="block container  py-2" v-for="(habit, habit_index) in habits">  
    <p class="py-2"> {{ habit.name }}</p>
    <div class="flex container">
      <div class="w-1/6 block border-r last:border-r-0" v-for="item in items[habit_index]">
        <div v-on:click="select(item)" :class="computedClass(item)">
        </div>
      </div>
    </div>
    </div>
    <div class="flex container">
      <button class="bg-green-500 hover:bg-blue-700 text-white font-bold py-2
      px-4 rounded my-2 mx-2 mx-auto" v-on:click="action('success')">
        ✅
      </button>
      <button class="bg-red-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded my-2 mx-2 mx-auto" 
              v-on:click="action('fail')">
        ❌
      </button>
      <button class="bg-gray-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded my-2 mx-2 mx-auto"
              v-on:click="action('pass')">
        ⚪️
      </button>
  </div>
  </Layout>
</template>
<script>
import dates from '../lib/dates.js';
export default {
  metaInfo: {
    title: 'Hello, world!'
  },
  data () {
    return {
      items:  [],
      monday: dates.getMonday(new Date()),
      week: [
        {day:'Monday'},
        {day:'Tuesday'}, 
        {day:'Wednesday'},
        {day:'Thursday'}, 
        {day:'Friday'}, 
        {day:'Saturday'},
        {day:'Sunday'}],

      habits: [],
      iMap: {},
      grooves: {
        'default': 'py-4 border-2 ',
        'none': 'bg-gray-200 border-gray-200 ',
        'selected': 'border-blue-500 ', 
        'success': 'bg-green-500 border-green-500 ',
        'fail': 'bg-red-500 border-red-500 ',
        'pass': 'stripes border-white '}
    }
  },
  mounted () {
    const id = this.$store.getters.id;
    this.$http
    .get('/api/habits/'+ id)
      .then(response => (this.mapHabits(response)));
    let startDate = this.monday.format();
    let endDate = dates.addDays(this.monday, 6).format();
    console.log(startDate);
    let url = '/api/grooves/' + id +
      '?start_date=' + startDate + '&end_date=' + endDate;
    console.log(url);
    this.$http.get(url).then(response => (this.mapper(response)));
  },
  methods: {
    generateWeek: function (habit_id) {
      let monday = this.monday;
      return [{day:'Monday', date: monday, clicked:false, groove: 'none',
        habit: habit_id}, 
      {day:'Tuesday', date: dates.addDays(monday, 1), clicked:false, groove:
        'none', habit: habit_id}, 
      {day:'Wednesday', date: dates.addDays(monday, 2), clicked:false, groove: 'none', habit: habit_id},
      {day:'Thursday', date: dates.addDays(monday, 3), clicked:false, groove: 'none', habit: habit_id}, 
      {day:'Friday', date: dates.addDays(monday, 4), clicked:false, groove: 'none', habit: habit_id}, 
      {day:'Saturday',date: dates.addDays(monday, 5), clicked:false, groove: 'none', habit: habit_id},
      {day:'Sunday',date: dates.addDays(monday, 6), clicked:false, groove: 'none', habit: habit_id}];
    },
    mapHabits: function (resp) {
      let habits = resp.data;
      this.habits = habits;
      let items = [];
      console.log(habits);
      for (let i= 0; i < habits.length; i++) {
        items.push(this.generateWeek(habits[i].id));
        console.log("Creating entry ("+i+", " + habits[i].id+ ")");
        this.iMap[habits[i].id] = i;
      }
      this.items = items;
    },
    mapper: function (data) {
      var items = data.data;
      console.log(data.data);
      for(var i = 0; i < items.length; i++){
        let current_date = new Date(items[i].date);
        let k = this.iMap[items[i].habit_id];
        console.log(k);
        for(var j = 0; j < this.items[k].length; j++) {
          if(dates.sameDate(this.items[k][j].date, current_date)){
            this.items[k][j].groove = items[i].state;
          }
        }
      }
    },
    select: function (item) {
      item.clicked = !item.clicked;
      //this.items[habit_index][index].clicked = !this.items[habit_index][index].clicked;
    },
    computedClass: function(item) {
      var ret = this.grooves['default'];
      ret += this.grooves[item.groove];
      if(item.clicked) {
        ret += this.grooves['selected'];
      }
      return ret;
    },
    updateGroove: function(groove) {
      this.$http.patch('/api/groove', 
          {owner_id: parseInt(localStorage.getItem('user_id')), 
            state: groove.groove, 
            habit_id: parseInt(groove.habit),
            date: groove.date}).then((response) => {
              console.log(response)
            }).catch((error) => {console.log(error)});
    },
    action: function (groove) {
      console.log(this.items[0][0] + " " + groove);
      for(let i = 0; i < this.items.length; i++) {
        for(var j = 0; j < this.items[i].length; j++) {
          if (this.items[i][j].clicked) {
            this.items[i][j].groove = groove;
            this.items[i][j].clicked = false;
            this.updateGroove(this.items[i][j]);
          }
        }
      }
    },
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
