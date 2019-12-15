import moment from 'moment';

var dates = {
  sameDate: function (d1, d2) {
    let _d1 = moment(d1).utc();
    let _d2 = moment(d2).utc();
    return _d1.isSame(_d2, 'year') && _d1.isSame(_d2, 'month') && _d1.isSame(_d2, 'day');
  },

  getMonday: function (d) {
    d = moment(d).utc();
    return d.day(0);
  },
  addDays: function (monday, days) {
    var date = moment(monday).utc();
    date.add(days, 'day');
    return date;
  }
};

export {dates as default};
