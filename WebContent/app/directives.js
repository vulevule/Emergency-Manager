eManager.filter('volFilter', function() {
  return function(input) {
    return input == null ? "Not assigned yet" : (input.name + " " + input.surname);
  };
});
