
var dates = {
  sameDate: function (d1, d2) {
    return d1.getUTCDate() === d2.getUTCDate() && d1.getUTCMonth() ===
      d2.getUTCMonth() &&
      d1.getUTCFullYear() === d2.getUTCFullYear();
  },

  getMonday: function (d) {
    d = new Date(d);
    let day = d.getDay(),
      diff = d.getDate() - day + (day == 0 ? -6:1); // adjust when day is sunday
    return new Date(d.setDate(diff));
  },
  addDays: function (monday, days) {
    var date = new Date(monday);
    date.setDate(date.getDate() + days);
    return date;
  }
};

export {dates as default};
