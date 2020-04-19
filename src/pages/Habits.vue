<template>
  <Layout>
    <div>
      <div style="display:flex; justify-content: space-between">
      <button id="previousWeek" class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
      px-4 rounded my-2 " v-on:click="previousWeek()" v-show="hasHabits">
        <icon_previous class="w-5"/>
      </button>
        <button id="nextWeek" class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
        px-4 rounded my-2 " v-on:click="nextWeek()" v-show="isPreviousWeek">
          <icon_next class="w-5" />
        </button>
      </div>
      <p id="no-habits" class="h1 text-center" v-show="!hasHabits"> You don't track any habits yet! Click the
      button below to create your very first habit 🎉 </p>
      <button id="add-habit" class="bg-max_blue-light hover:bg-max_blue-dark text-white font-bold py-2
      px-4 rounded my-2 mx-2 mx-auto" v-on:click="newHabit()">
        <icon_plus class="w-5"/>
      </button>
        <div class="block py-2" v-show="creating">
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
    <div class="flex container" v-show="hasHabits">
        <div class="py-2 w-1/6 block border-r last:border-r-0 text-center bg-gray-100" v-for="day in week">
          <div v-show="xl">
          {{ day.day }} 
          </div>
          <div v-show="!xl">
          {{ day.day_xs }} 
          </div>
            <div class="text-center" v-show="day.date">
              {{ day.date }} 
          </div>
        </div>
  </div>
  <section class="block container  py-2" v-for="habit in habits" v-show="hasHabits">  
    <span style="display:flex; justify-content: space-between">
    <span class="py-2"> {{ habit.name }} </span>
    <span class="mx-3" v-show="habit.current_streak>1">{{ habit.current_streak }}🔥</span>
    </span>
    <div class="flex container">
      <div v-bind:id="habit.name+'-'+week[index].day" class="w-1/6 block border-r last:border-r-0"
        v-for="(item, index) in items[habit.name]">
        <div v-bind:id="habit.name+'-'+week[index].day+'-groove'"
             v-on:click="select(item)"
             :class="computedClass(item)" :key="item.id">
        </div>
      </div>
    </div>
    </section>

    <div class="flex container" v-show="hasHabits">
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
      rutta_url: process.env.GRIDSOME_API_URL,
      feedback: "",
      items:  [],
      current_date: new Date(),
      current_monday: dates.getMonday(new Date()),
      actual_monday: dates.getMonday(new Date()),
      isPreviousWeek: false,
      creating: false,
      habitName: "",
      hasHabits: false,
      isWeekView: true,
      compiled: typeof window !== 'undefined' && window,
      xl: this.compiled && window.innerWidth >= 960,
      week: [
        {day_xs:'Mon', day:'Monday',      date: dates.getMonday(new
          Date()).format("Do")},
        {day_xs:'Tue', day:'Tuesday',     date: dates.addDays(dates.getMonday(new Date()), 1).format("Do")}, 
        {day_xs:'Wed', day:'Wednesday',   date: dates.addDays(dates.getMonday(new Date()), 2).format("Do")},
        {day_xs:'Thu', day:'Thursday',    date: dates.addDays(dates.getMonday(new Date()), 3).format("Do")}, 
        {day_xs:'Fri', day:'Friday',      date: dates.addDays(dates.getMonday(new Date()), 4).format("Do")}, 
        {day_xs:'Sat', day:'Saturday',    date: dates.addDays(dates.getMonday(new Date()), 5).format("Do")},
        {day_xs:'Sun', day:'Sunday',      date: dates.addDays(dates.getMonday(new Date()), 6).format("Do")}],
      iMap: {},
      habits: [],
      grooves: {
        'default': 'py-4 border-2 ',
        'none': 'bg-gray-200 cursor-pointer ',
        'selected': 'border-max_blue-dark cursor-pointer ', 
        'success': 'bg-ocean_green-light cursor-pointer ',
        'fail': 'bg-tango_pink-light cursor-pointer ',
        'pass': 'stripes cursor-pointer ',
        'disabled': 'bg-gray-400 '
        },
        grooveBorders: {
        'none': ' border-gray-200 ',
        'selected': 'border-max_blue-dark ', 
        'success': ' border-ocean_green-light ',
        'fail': ' border-tango_pink-light ',
        'pass': ' border-white ',
        'disabled': 'border-gray-400 '
        }
    }
  },
  mounted () {
    window.addEventListener("resize", () => {
      //console.log("rezising");
      if (typeof window !== 'undefined' && window) {
        this.xl = window.innerWidth >= 960;
      }
    });
    const start_date = localStorage.getItem('start_date');
    if (start_date) {
        //console.log("Found current date in localStorage");
        this.current_monday = this.$moment(start_date);
    } else {
        this.current_monday = dates.getMonday(new Date()); 
    }
    this.setIsPreviousWeek();
    this.updateWeek();
    this.getHabits();
  },
  methods: {
    getHabits: function() {
      let startDate = this.current_monday.utc().format('YYYY-MM-DD');
      let endDate = dates.addDays(this.current_monday, 6).utc().format('YYYY-MM-DD');
      let url = this.rutta_url + '/api/habits?start_date=' + startDate + '&end_date=' + endDate;
      this.$http.get(url)
        .then(response => {
          this.mapper(response);
        }).catch((error) => {
          console.log("An error has occured!");
          console.log(error);
          this.hasHabits = false;
          this.iMap = {};
          this.habits = [];
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
      this.$http.post(this.rutta_url +'/api/habit',{owner_id: parseInt(id), name:
        this.habitName}).then(response => {
          location.reload();
        }).catch(err => {
          this.positive_feedback = false;
          this.feedback = handler.handleError(err, 
            "");
        });
    },
    updateWeek: function () {
        this.week[0]['date'] = this.current_monday.format("Do");
        for (let i = 1; i < 7; i++) {
            this.week[i]['date'] = dates.addDays(this.current_monday, i).format("Do");
        }
        localStorage.setItem('start_date', this.current_monday);
    },
    setIsPreviousWeek: function() {
      this.isPreviousWeek = dates.isAfter(this.actual_monday, this.current_monday) || !dates.sameDate(this.actual_monday,
          this.current_monday);
    },
    previousWeek: function () {
      this.current_monday = dates.addDays(this.current_monday, -7);
      this.setIsPreviousWeek();
      this.updateWeek();
      this.getHabits();
    },
    nextWeek: function () {
      this.current_monday = dates.addDays(this.current_monday, 7);
      this.setIsPreviousWeek();
      this.updateWeek();
      this.getHabits();
    },
    generateWeek: function (habit, id) {
      let monday = this.current_monday;
      return [
        {day:'Monday', date: monday, clicked:false, groove: 'none', habit: habit, user_habit_id: id, id: 0}, 
        {day:'Tuesday', date: dates.addDays(monday, 1), clicked:false, groove: 'none', habit: habit, user_habit_id: id   ,id:0}, 
        {day:'Wednesday', date: dates.addDays(monday, 2), clicked:false, groove: 'none', habit: habit, user_habit_id: id ,id:0},
        {day:'Thursday', date: dates.addDays(monday, 3), clicked:false, groove: 'none', habit: habit, user_habit_id: id  ,id:0}, 
        {day:'Friday', date: dates.addDays(monday, 4), clicked:false, groove: 'none', habit: habit, user_habit_id: id    ,id:0}, 
        {day:'Saturday',date: dates.addDays(monday, 5), clicked:false, groove: 'none', habit: habit, user_habit_id: id  , id:0},
        {day:'Sunday',date: dates.addDays(monday, 6), clicked:false, groove: 'none', habit: habit, user_habit_id: id     ,id:0}];
    },
    updateHabit(habit) {
      //console.log(habit);
      for(let i = 0; i < habit.grooves.length; i++) {
        let current_date = this.$moment(habit.grooves[i].date);
        for(let j = 0; j < this.items[habit.name].length; j++) {
          if(dates.sameDate(this.items[habit.name][j].date, current_date)) {
            this.items[habit.name][j].groove = habit.grooves[i].state || 'none';
            break;
          }
        }
      }
      this.habits[habit.name].current_streak = habit.current_streak;
    },
    mapper: function (data) {
      let items = data.data;
      //console.log(items);
      if (this.isWeekView) {
        this.habitsNames = Object.keys(items);
        this.habits = items;
        this.hasHabits = Object.keys(items).length > 0;
        for(let i = 0;i < this.habitsNames.length; i++) {
          this.items[this.habitsNames[i]] = this.generateWeek(this.habitsNames[i], items[this.habitsNames[i]].id);
          this.updateHabit(items[this.habitsNames[i]]);
        }
      }
      //console.log(this.items);
    },
    select: function (item) {
      //console.log(this.items);
      if (dates.isAfter(item.date, new Date())) {
        console.log("User clicked a future groove.");
        return;
      } else {
        item.clicked = !item.clicked;
      }
      item.id += 1;
      this.$forceUpdate();
    },
    computedClass: function(item) {
        let ret = this.grooves['default'];
        if (dates.isAfter(item.date, new Date())) {
            ret += this.grooves['disabled'];
            ret += this.grooveBorders['disabled'];
            return ret;
        } 
        ret += this.grooves[item.groove];
      if(item.clicked) {
        ret += this.grooveBorders['selected'];
      } else {
        ret += this.grooveBorders[item.groove];
      }
      return ret;
    },
    updateGroove: function(groove) {
      //console.log(groove);
      this.$http.patch(this.rutta_url + '/api/groove', 
          {owner_id: parseInt(localStorage.getItem('user_id')), 
            state: groove.groove, 
            user_habit_id: parseInt(groove.user_habit_id),
            date: groove.date.utc().format("YYYY-MM-DD")}).then((_) => {
              let startDate = this.current_monday.utc().format('YYYY-MM-DD');
              let endDate = dates.addDays(this.current_monday, 6).utc().format('YYYY-MM-DD');
              return this.$http.get(this.rutta_url + '/api/habit/' +
                groove['user_habit_id']+'?start_date=' +startDate
                +'&end_date='+endDate);}).then((response) => {
                  //console.log(response);
                  this.items[response.data.name] = this.generateWeek(response.data.name, response.data.id);
                  this.updateHabit(response.data);
                }).then((_) => this.$forceUpdate()).catch(error => {console.log(error.response)});
    },
    capitalizeFirstLetter: function (string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    },
    action: function (groove) {
      let items = Object.keys(this.items);
      for(let i = 0; i < items.length; i++) {
        for(let j = 0; j < this.items[items[i]].length; j++) {
          if (this.items[items[i]][j].clicked) {
            this.items[items[i]][j].groove = groove;
            this.items[items[i]][j].clicked = false;
            this.$forceUpdate();
            this.updateGroove(this.items[items[i]][j]);
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
