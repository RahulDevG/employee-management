function autoCloseAlert(){
  $("#alert-success").fadeTo(2000, 500).slideUp(500, function(){
    $("#alert-success").slideUp(500);
  });

  $("#alert-danger").fadeTo(2000, 500).slideUp(500, function(){
    $("#alert-danger").slideUp(500);
  });
}
