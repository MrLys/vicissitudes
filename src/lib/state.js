import dates from './dates.js';

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
        return JSON.parse(localStorage.getItem('habits'));
    },
    setHabits: function (habits) {
        setItem('habits', JSON.stringify(habits));
    },
    getGrooves: function (habit) {
        return JSON.parse(localStorage.getItem(habit));
    },
    setGrooves: function (habit, grooves) {
       setItem(habit, JSON.stringify(grooves));
    },
    getWeek: function (which) {
        if (which > 0) return getItem(nextWeek) || [];
        else if (which < 0) return getItem(previousWeek) || [];
        else  return getItem(currentWeek) || [];
    },
    setWeek: function (week, which) {
        if (which > 0 ) setItem(nextWeek, JSON.stringify(week));
        else if (which < 0 ) setItem(previousWeek, JSON.stringify(week));
        else setItem(currentWeek, JSON.stringify(week));
    },
    getActualMonday: function () {
        const date = getItem(actualMonday) || dates.getMonday(new Date());
        setItem(actualMonday, date);
        return date;
    },
    getCurrentMonday: function () {
        return getItem(currentMonday) || this.getActualMonday();
    },
    setCurrentMonday: function (date) {
        setItem(currentMonday, date || this.getActualMonday);
    },
    isWeekView: function () {
        return getItem(view) === 'true';
    },
    setView: function (isWeek) {
        setItem(view, isWeek === 'true');
    },
    hasHabits: function () {
        return this.hasHabits !== '';
    },
}
