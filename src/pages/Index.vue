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
export default {
  metaInfo: {
    title: 'Hello, world!'
  },
  data () {
    return {
      items:  [
      {day:'Monday', clicked:false, choice: 'none'}, 
      {day:'Tuesday', clicked:false, choice: 'none'}, 
      {day:'Wednesday', clicked:false, choice: 'none'},
      {day:'Thursday', clicked:false, choice: 'none'}, 
      {day:'Friday', clicked:false, choice: 'none'}, 
      {day:'Saturday', clicked:false, choice: 'none'},
      {day:'Sunday', clicked:false, choice: 'none'}],
      choices: {
        'default': 'py-4 border-2 ',
        'none': 'bg-gray-200 border-gray-200 ',
        'selected': 'border-blue-500 ', 
        'success': 'bg-green-500 border-green-500 ',
        'fail': 'bg-red-500 border-red-500 ',
        'pass': 'stripes border-white '}
    }
  },
  mounted () {
    this.$http
    .get('/api/choices')
      .then(response => (this.mapper(response)));
  },
  methods: {
    mapper: function (data) {
      console.log(data.data['friday']);
      var items = data.data;
      for(var i = 0; i < this.items.length; i++){
        console.log(this.items[i].day.toLowerCase());
        this.items[i].choice = items[this.items[i].day.toLowerCase()];
      }
    },
    select: function (index) {
      this.items[index].clicked = !this.items[index].clicked;
    },
    computedClass: function(index) {
      var ret = this.choices['default'];
      ret += this.choices[this.items[index].choice];
      if(this.items[index].clicked) {
        ret += this.choices['selected'];
      }
      return ret;
    },
    action: function (choice) {
      for(var i = 0; i < this.items.length; i++) {
        if (this.items[i].clicked) {
          this.items[i].choice = choice;
          this.items[i].clicked = false;
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
