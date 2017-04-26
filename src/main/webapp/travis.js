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
    return '<i class="fa fa-question-circle-o" aria-hidden="true" alt="'+state+'"></i>';
  }
};
