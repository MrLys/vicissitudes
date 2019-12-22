var handler = {
  handleError: function (err, unauthorized) {
    console.log(err.response);
    if(err.response.status === 401) {
      return unauthorized;
    } else if (err.response.status === 500) {
      return "An unexcpected error occured!";
    } else if (err.response.status === 400){
      if (err.response.data.errors.email) {
      return "Please enter a valid email! 🤓";
      } else if (err.response.data.errors.username){
        return "Invalid username";
      } else {
        return err.response.data.errors;
      }
    } else {
      return err.response.data.error;
    }
  }
}
export {handler as default};
