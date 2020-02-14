<template>
  <Layout>
    <div>
      <div style="display:flex; justify-content: space-between">
      <button id="previousWeek" class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
      px-4 rounded my-2 " v-on:click="previousWeek()">
        <icon_previous class="w-5"/>
      </button>
        <button id="nextWeek" class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
        px-4 rounded my-2 " v-on:click="nextWeek()" v-if="isPreviousWeek">
          <icon_next class="w-5" />
        </button>
      </div>
      <p class="h1 text-center" v-if="!hasHabits"> You don't track any habits yet! Click the
      button below to create your very first habit 🎉 </p>
      <button id="add-habit" class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
      px-4 rounded my-2 mx-2 mx-auto" v-on:click="newHabit()">
        <icon_plus class="w-5"/>
      </button>
        <div class="block py-2" v-if="creating">
          <div class="flex">
          <input id="habit-field" class="bg-white focus:outline-none focus:shadow-outline border
          border-gray-300 rounded-lg py-2 px-4 block w-full appearance-none
          leading-normal" v-model="habitName" type="text" minlength="2" maxlength="255" placeholder="Name your new habit! E.g exercising" v-on:keyup.enter="createHabit()">
        <button id="save-habit" class="rounded border-2 
          bg-ocean_green-light hover:bg-ocean_green-dark p-2 mx-2" v-on:click="createHabit()">
          Save
        </button>
        </div>
        <p class="text-red-500 m-2"> {{ feedback }}</p>
        </div>
    </div>
    <div class="flex container" v-if="hasHabits">
        <div class="py-2 w-1/6 block border-r last:border-r-0 text-center bg-gray-100" v-for="day in week">
          {{ day.day }} 
        </div>
  </div>

  <div class="block container  py-2" v-for="(habit, habit_index) in habits"
    v-if="hasHabits">  
    <p class="py-2"> {{ habit.name }}</p>
    <div class="flex container">
      <div v-bind:id="habit.name+'-'+week[index].day" class="w-1/6 block border-r last:border-r-0"
        v-for="(item, index) in items[habit_index]">
        <div v-bind:id="habit.name+'-'+week[index].day+'-groove'" v-on:click="select(item)" :class="computedClass(item)">
        </div>
      </div>
    </div>
    </div>

    <div class="flex container" v-if="hasHabits">
      <button id="success-button" class="bg-ocean_green-light hover:bg-ocean_green-dark  text-white font-bold py-2
      px-4 rounded my-2 mx-2 mx-auto" v-on:click="action('success')">
        <icon_success class="w-6"/>
      </button>

      <button id="fail-button" class="bg-tango_pink-light hover:bg-tango_pink-dark text-white font-bold py-2 px-4 rounded my-2 mx-2 mx-auto" 
              v-on:click="action('fail')">
        <icon_fail class="w-6"/>
      </button>
      <button id="pass-button" class="bg-gray-300 hover:bg-gray-500 text-white font-bold py-2 px-4 rounded my-2 mx-2 mx-auto"
              v-on:click="action('pass')">
        <icon_pass class="w-6"/>
      </button>
  </div>
  </Layout>
</template>
<script>
import dates from '../lib/dates.js';
import icon_success from '~/assets/svgs/success.svg'
import icon_fail from '~/assets/svgs/error.svg'
import icon_pass from '~/assets/svgs/minus.svg'
import icon_plus from '~/assets/svgs/plus.svg'
import icon_next from '~/assets/svgs/fast-forward.svg'
import icon_previous from '~/assets/svgs/rewind.svg'
import handler from '../lib/responseHandler.js';
export default {
  metaInfo: {
    title: 'Hello, world!'
  },components: {
     icon_success,
     icon_fail,
     icon_pass,
    icon_next,
    icon_previous,
    icon_plus
    },
  data () {
    return {
      feedback: "",
      items:  [],
      current_monday: dates.getMonday(new Date()),
      actual_monday: dates.getMonday(new Date()),
      isPreviousWeek: false,
      creating: false,
      habitName: "",
      hasHabits: false,
      isWeekView: true,
      week: [
        {day:'Monday'},
        {day:'Tuesday'}, 
        {day:'Wednesday'},
        {day:'Thursday'}, 
        {day:'Friday'}, 
        {day:'Saturday'},
        {day:'Sunday'}],
      iMap: {},
      habits: [],
      grooves: {
        'default': 'py-4 border-2 ',
        'none': 'bg-gray-200 ',
        'selected': 'border-max_blue-dark ', 
        'success': 'bg-ocean_green-light ',
        'fail': 'bg-tango_pink-light ',
        'pass': 'stripes '
        },
        grooveBorders: {
        'none': ' border-gray-200 ',
        'selected': 'border-max_blue-dark ', 
        'success': ' border-ocean_green-light ',
        'fail': ' border-tango_pink-light ',
        'pass': ' border-white '
        }
    }
  },
  mounted () {
    this.getHabits();
  },
  methods: {
    getHabits: function() {
      let startDate = this.current_monday.format("YYYY-MM-DD");
      let endDate = dates.addDays(this.current_monday, 6).format("YYYY-MM-DD");
      let url = '/api/habits?start_date=' + startDate + '&end_date=' + endDate;
      console.log('Getting habits from ' + url);
      this.$http.get(url)
        .then(response => {
          this.mapper(response);
        }).catch((error) => {
          console.log("An error has occured!");
          console.log(error);
          this.feedback = handler.handleError(error, "");
          this.positive_feedback = false;
          console.log(error.response);
        });
    },
    newHabit: function() {
      this.creating = true;
    },
    createHabit: function (){
      if (this.habitName.length < 2) {
        this.feedback = "Please enter a longer habit name";
        return;
      }
      let id = localStorage.getItem('user_id');
      this.$http.post('/api/habit',{owner_id: parseInt(id), name:
        this.habitName}).then(response => {
          location.reload();
        }).catch(err => {
          this.positive_feedback = false;
          this.feedback = handler.handleError(err, 
            "");
        });
    },
    previousWeek: function () {
      this.current_monday = dates.addDays(this.current_monday, -7);
      this.isPreviousWeek = dates.isAfter(this.actual_monday, this.current_monday);
      this.getHabits();
    },
    nextWeek: function () {
      this.current_monday = dates.addDays(this.current_monday, 7);
      this.isPreviousWeek = dates.isAfter(this.actual_monday,
        this.current_monday) || !dates.sameDate(this.actual_monday,
          this.current_monday);
      this.getHabits();
    },
    generateWeek: function (user_habit_id) {
      let monday = this.current_monday;
      return [{day:'Monday', date: monday, clicked:false, groove: 'none',
        habit: user_habit_id}, 
      {day:'Tuesday', date: dates.addDays(monday, 1), clicked:false, groove:
        'none', habit: user_habit_id}, 
      {day:'Wednesday', date: dates.addDays(monday, 2), clicked:false, groove: 'none', habit: user_habit_id},
      {day:'Thursday', date: dates.addDays(monday, 3), clicked:false, groove: 'none', habit: user_habit_id}, 
      {day:'Friday', date: dates.addDays(monday, 4), clicked:false, groove: 'none', habit: user_habit_id}, 
      {day:'Saturday',date: dates.addDays(monday, 5), clicked:false, groove: 'none', habit: user_habit_id},
      {day:'Sunday',date: dates.addDays(monday, 6), clicked:false, groove: 'none', habit: user_habit_id}];
    },
    mapHabits: function (habits) {
      let items = [];
      let i = 0;
      let keys = Object.keys(habits);
      for (let i = 0; i < keys.length; i++) {
        items.push(this.generateWeek(habits[keys[i]].id));
        this.iMap[habits[keys[i]].id] = i;
      }
      this.items = items;
    },
    mapHabitsResp: function (resp) {
      let habits = resp;
      let keys = Object.keys(habits);
      let habit_array = []
      for(let i = 0; i < keys.length; i++) {
        habits[keys[i]].name = this.capitalizeFirstLetter(habits[keys[i]].name);
        habit_array[i] = habits[keys[i]];
      }
      this.habits = habit_array;
      this.hasHabits = Object.keys(habits).length > 0;
      console.log(this.hasHabits);
      this.mapHabits(habits);
    },
    mapper: function (data) {
      let items = data.data;
      if (this.isWeekView) {
        this.mapHabitsResp(items);
        let keys = Object.keys(items); 
        for(let i = 0; i < keys.length; i++){
            for(let m = 0; m < items[keys[i]].grooves.length; m++) {
                let current_date =
                items[keys[i]].grooves[m].date = this.$moment(items[keys[i]].grooves[m].date);
                let n = items[keys[i]].id;
                let k = this.iMap[n];
                for(let j = 0; j < this.items[k].length; j++) {
                    if(dates.sameDate(this.items[k][j].date, current_date)){
                        this.items[k][j].groove = items[keys[i]].grooves[m].state;
                        break;
                    }
                }
            }
        }
      }
    },
    select: function (item) {
      item.clicked = !item.clicked;
      console.log(item.date);
    },
    computedClass: function(item) {
      let ret = this.grooves['default'];
      ret += this.grooves[item.groove];
      if(item.clicked) {
        ret += this.grooveBorders['selected'];
      } else {
        ret += this.grooveBorders[item.groove];
      }
      console.log(ret);
      return ret;
    },
    updateGroove: function(groove) {
      this.$http.patch('/api/groove', 
          {owner_id: parseInt(localStorage.getItem('user_id')), 
            state: groove.groove, 
            user_habit_id: parseInt(groove.habit),
            date: groove.date.format("YYYY-MM-DD")}).then((response) => {
              console.log(response)
            }).catch((error) => {console.log(error.response)});
    },
    capitalizeFirstLetter: function (string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    },
    action: function (groove) {
      console.log(this.items[0][0] + " " + groove);
      for(let i = 0; i < this.items.length; i++) {
        for(let j = 0; j < this.items[i].length; j++) {
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
