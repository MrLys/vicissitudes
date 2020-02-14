import moment from 'moment';

var dates = {
    sameDate: function (d1, d2) {
        let _d1 = moment(d1);
        let _d2 = moment(d2);
        return _d1.isSame(_d2, 'year') && _d1.isSame(_d2, 'month') && _d1.date() == _d2.date();
    },
    addDays: function (monday, days) {
        var date = moment(monday).utc();
        date.add(days, 'day');
        return date.utc();
    },
    getMonday: function (d) {
        d = moment(d);//.utc();
        if (d.day() == 0) {// sunday
            d = this.addDays(d, -1);
        }
        return d.day(1); // First day of week is sunday -.-
    },
    isAfter: function (day1, day2) {
        return moment(day1).isAfter(day2);
    }
};

export {dates as default};
