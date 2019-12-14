<template>
  <Layout>

    <!-- Learn how to use images here: https://gridsome.org/docs/images -->
    <div class="flex container">
      <div class="w-1/6 block border-r last:border-r-0" v-for="(item, index) in items">
        <div class="py-2 text-center bg-gray-100 ">
          {{ item.day }} 
        </div>
        <div v-on:click="select(index)" :class="computedClass(index)">
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
    let monday = dates.getMonday(new Date());
    this.items = 
      [{day:'Monday', date: monday, clicked:false, groove: 'none'}, 
      {day:'Tuesday', date: dates.addDays(monday, 1), clicked:false, groove: 'none'}, 
      {day:'Wednesday', date: dates.addDays(monday, 2), clicked:false, groove: 'none'},
      {day:'Thursday', date: dates.addDays(monday, 3), clicked:false, groove: 'none'}, 
      {day:'Friday', date: dates.addDays(monday, 4), clicked:false, groove: 'none'}, 
      {day:'Saturday',date: dates.addDays(monday, 5), clicked:false, groove: 'none'},
      {day:'Sunday',date: dates.addDays(monday, 6), clicked:false, groove: 'none'}];
    const id = this.$store.getters.id;
    let url = '/api/grooves/' + id +
      '/1?start_date=2019-12-02&end_date=2019-12-09';
    this.$http
    .get(url)
      .then(response => (this.mapper(response)));
  },
  methods: {
    mapper: function (data) {
      var items = data.data;
      for(var i = 0; i < items.length; i++){
        let current_date = new Date(items[i].date);
        for(var j = 0; j < this.items.length; j++) {
          if(dates.sameDate(this.items[j].date, current_date)){
            this.items[j].groove = items[i].state;
          }
        }
      }
    },
    select: function (index) {
      this.items[index].clicked = !this.items[index].clicked;
    },
    computedClass: function(index) {
      var ret = this.grooves['default'];
      ret += this.grooves[this.items[index].groove];
      if(this.items[index].clicked) {
        ret += this.grooves['selected'];
      }
      return ret;
    },
    updateGroove: function(groove) {
      this.$http.post('/api/groove', 
        {data: 
          {owner_id: localStorage.getItem('user_id'), 
            state: groove.groove, 
            habit_id: 1,
            date: groove.date}}).then((response) => {
          console.log(response)}).catch((error) => {console.log(error)});
    },
    action: function (groove) {
      for(var i = 0; i < this.items.length; i++) {
        if (this.items[i].clicked) {
          this.items[i].groove = groove;
          this.items[i].clicked = false;
          this.updateGroove(this.items[i]);
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
