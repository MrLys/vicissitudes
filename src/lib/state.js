import dates from './dates.js';
import moment from 'moment';

const nextWeek = 'nextWeek';
const previousWeek = 'previousWeek';
const currentWeek = 'currentWeek';
const currentMonday = 'currentMonday';
const actualMonday = 'actualMonday';
const view = 'weekView';

function getItem(key) {
    if (typeof window !== 'undefined' && window) {
        return window.localStorage.getItem(key);
    }
}
function setItem(key, value) {
    if (typeof window !== 'undefined' && window) {
        window.localStorage.setItem(key, value);
    }

}
function removeItem(key) {
    if (typeof window !== 'undefined' && window) {
        return window.localStorage.removeItem(key);
    }
}
let state = {
    getToken: function () {
        return getItem('token');
    },
    setToken: function (token) {
        setItem('token', token || '');
    },
    logout: function () {
        removeItem('token');
        setItem('loggedIn', 'false');
    },
    loggedIn: function () {
        const token = this.getToken();
        const loggedIn = getItem('loggedIn') === 'true' && token !== '';
        if (!loggedIn) this.logout();
        return loggedIn;
    },
    getHabits: function () {
      if (typeof window !== 'undefined' && window) {
        return JSON.parse(getItem('habits'));
      }
    },
    setHabits: function (habits) {
        setItem('habits', JSON.stringify(habits));
    },
    getGrooves: function (habit) {
      if (typeof window !== 'undefined' && window) {
        return JSON.parse(getItem(habit));
      }
    },
    setGrooves: function (habit, grooves) {
       setItem(habit, JSON.stringify(grooves));
    },
    getDefaultWeek: function (monday) {
      let week = [{day_xs:'Mon', day:'Monday', date: monday.format("Do")},
        {day_xs:'Tue', day:'Tuesday',     date: dates.addDays(monday, 1).format("Do")}, 
        {day_xs:'Wed', day:'Wednesday',   date: dates.addDays(monday, 2).format("Do")},
        {day_xs:'Thu', day:'Thursday',    date: dates.addDays(monday, 3).format("Do")}, 
        {day_xs:'Fri', day:'Friday',      date: dates.addDays(monday, 4).format("Do")}, 
        {day_xs:'Sat', day:'Saturday',    date: dates.addDays(monday, 5).format("Do")},
        {day_xs:'Sun', day:'Sunday',      date: dates.addDays(monday, 6).format("Do")}];
      return week;
    },
    getCurrentWeek: function () {
      if (typeof window !== 'undefined' && window) {
        return JSON.parse(getItem(getItem(currentWeek) || '')) || this.getDefaultWeek(this.getCurrentMonday());
      }
    },
    setCurrentWeek: function () {
      let week = this.getDefaultWeek(this.getCurrentMonday());
      const currentMon = moment(this.getCurrentMonday());
      const weekYearNum = 'week' + currentMon.year() + currentMon.week();
      setItem(currentWeek, weekYearNum);
      setItem(weekYearNum, JSON.stringify(week));
    },
    getNextWeek: function () {
      return getItem(getItem(nextWeek) || '') || [];
    },
    setNextWeek: function (week){
      const currentMon = moment(this.getCurrentMonday()).add(7, 'd');
      const weekYearNum = 'week' + currentMon.year() + currentMon.week();
      setItem(nextWeek, weekYearNum);
      setItem(weekYearNum, JSON.stringify(week));
    },
    getPreviousWeek: function () {
      return getItem(getItem(previousWeek) || '') || [];
    },
    setPreviousWeek: function () {
      let week = this.getDefaultWeek(this.getCurrentMonday());
      const currentMon = moment(this.getCurrentMonday()).subtract(7, 'd');
      const weekYearNum = 'week' + currentMon.year() + currentMon.week();
      setItem(previousWeek, weekYearNum);
      setItem(weekYearNum, JSON.stringify(week));
    },
    getActualMonday: function () {
        const date = getItem(actualMonday) || dates.getMonday(new Date().toISOString()).toISOString();
        setItem(actualMonday, date);
        return moment(date);
    },
    getCurrentMonday: function () {
        return moment(getItem(currentMonday) || this.getActualMonday());
    },
    setCurrentMonday: function (date) {
        setItem(currentMonday, date.toISOString() || this.getActualMonday());
    },
    isWeekView: function () {
        return getItem(view) !== 'false';
    },
    setView: function (isWeek) {
        setItem(view, isWeek === 'true');
    },
    hasHabits: function () {
        return this.hasHabits !== '';
    },
}

export {state as default};
