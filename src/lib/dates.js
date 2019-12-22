import moment from 'moment';

var dates = {
  sameDate: function (d1, d2) {
    let _d1 = moment(d1);
    let _d2 = moment(d2);
    return _d2.isSame(_d1, 'year') && _d2.isSame(_d1, 'month') && _d2.isSame(_d1, 'day');
  },

  getMonday: function (d) {
    d = moment(d).utc();
    return d.isoWeekday(1); // First day of week is sunday -.-
  },
  addDays: function (monday, days) {
    var date = moment(monday).utc();
    date.add(days, 'day');
    return date.utc();
  }
};

export {dates as default};
