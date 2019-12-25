var handler = {
  handleError: function (err, unauthorized) {
    console.log(err.response);
    if(err.response.status === 401) {
      return unauthorized;
    } else if (err.response.status === 500) {
      return "An unexcpected error occured!";
    } else if (err.response.status === 400){
      if (err.response.data.errors.email !== undefined) {
        return "Please enter a valid email! 🤓";
      } else if (err.response.data.errors.username !== undefined){
        return "Invalid username";
      } else {
        return err.response.data.errors;
      }
    } else if (err.response.status === 409) {
        return "You are already tracking that habit! 🤷🏻‍♂️";
    } else {
      return err.response.data.error;
    }
  }
}
export {handler as default};
