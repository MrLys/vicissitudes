import moment from 'moment';

var dates = {
  sameDate: function (d1, d2) {
    let _d1 = moment(d1);
    let _d2 = moment(d2);
    return _d2.isSame(_d1, 'year') && _d2.isSame(_d1, 'month') && _d2.isSame(_d1, 'day');
  },

  getMonday: function (d) {
    d = moment(d);
    return d.isoWeekday(1); // First day of week (1) is monday
  },
  addDays: function (monday, days) {
    var date = moment(monday);
    date.add(days, 'day');
    return date;
  }
};

export {dates as default};
