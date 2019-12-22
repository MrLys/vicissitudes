var handler = {
  handleError: function (err, unauthorized) {
    if(err.response.status === 401) {
      return unauthorized;
    } else if (err.response.status === 500) {
      return "An unexcpected error occured!";
    } else {
      return err.response.data.error;
    }
  }
}
export {handler as default};
