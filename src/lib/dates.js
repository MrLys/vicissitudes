import moment from 'moment';

var dates = {
    sameDate: function (d1, d2) {
        let _d1 = moment(d1);
        let _d2 = moment(d2);
        return _d1.isSame(_d2, 'year') && _d1.isSame(_d2, 'month') && _d1.date() == _d2.date();
    },
    addDays: function (monday, days) {
        let date = moment(monday).utc();
        date.add(days, 'day');
        return date.utc();
    },
    getMonday: function (d) {
        console.log(d);
        d = moment(d);//.utc();
        console.log(d);
        if (d.day() == 0) {// sunday
            d = this.addDays(d, -1);
        }
        return d.day(1); // First day of week is sunday -.-
    },
    isAfter: function (day1, day2) {
        let _d1 = moment(day1);
        let _d2 = moment(day2);
        return _d1.isAfter(_d2, 'year') || _d1.isAfter(_d2, 'month') || _d1.isAfter(_d2, 'date');
    }
};

export {dates as default};
