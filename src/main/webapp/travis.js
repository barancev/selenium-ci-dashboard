var stateIcon = function(state) {
  if (state == "pending") {
    return '<i class="fa fa-hourglass-o" aria-hidden="true"></i>';
  } else if (state == "running") {
    return '<i class="fa fa-spinner fa-spin" aria-hidden="true"></i>';
  } else if (state == "passed") {
    return '<i class="fa fa-check" aria-hidden="true"></i>';
  } else if (state == "failed") {
    return '<i class="fa fa-times" aria-hidden="true"></i>';
  } else if (state == "skipped") {
    return '<i class="fa fa-ban" aria-hidden="true"></i>';
  } else if (state == "cancelled") {
    return '<i class="fa fa-ban" aria-hidden="true"></i>';
  } else {
    return '<i class="fa fa-question-circle-o" aria-hidden="true" title="'+state+'"></i>';
  }
};

var getParameterByName = function(name, url) {
  if (!url) url = window.location.href;
  name = name.replace(/[\[\]]/g, "\\$&");
  var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, " "));
};
