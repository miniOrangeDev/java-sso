<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Main CSS-->
    <link rel="stylesheet" type="text/css" href="?resource=/includes/css/main.css">
    <!-- Font-icon css-->
    <link rel="stylesheet" type="text/css"
          href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Register - miniOrange Admin
    </title>
</head>
<body>
<section class="material-half-bg">
    <div class="cover">
    </div>
</section>
<section class="login-content">
     <#if isErrorMsg>

        <div class="alert alert-danger alert-dismissible fade show" role="alert">
          ${message}
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>

    </#if>


    <div class="logo">
        <h1>
            <img src="?resource=/resources/images/logo_large.png">
        </h1>
    </div>
    <div class="col-md-6">
        <div class="tile">
            <h3 class="tile-title">Register With Miniorange
            </h3>
            <form class="register_form" id="register_form" method="POST" action="">
                <input type="hidden" name="choice" value="register">
                <div class="tile-body">
                    <div class="form-group row">
                        <label class="control-label col-md-3">Email
                        </label>
                        <div class="col-md-8">
                            <input class="form-control col-md-8" type="email" name="email"
                                   placeholder="Enter email address" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="control-label col-md-3">Password
                        </label>
                        <div class="col-md-8">
                            <input class="form-control col-md-8" type="password" id="password" name="password"
                                   placeholder="Enter a password" minlength="6" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="control-label col-md-3">Confirm Password
                        </label>
                        <div class="col-md-8">
                            <input class="form-control col-md-8" type="password" id="confirmPassword" name="confirmPassword"
                                   placeholder="Re-type the password" minlength="6" required>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="control-label col-md-3">Use Case
                        </label>
                        <div class="col-md-8">
                            <input class="form-control col-md-8" type="text" id="purpose" name="purpose"
                                   placeholder="Use Case" minlength="6" >
                        </div>
                    </div>



                </div>
                <div class="tile-footer">
                  <table class='mo_saml_settings_table'>
                                    <tr>
                                      <td>&nbsp;
                                      </td>
                                      <td>
                                        <br>
                                        <input type='submit' name='register' value='Register'
                                               id='register'
                                               class='btn btn-primary'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <input type='button' name='mo_saml_goto_login'
                                               id='mo_saml_goto_login'
                                               value='Already have an account?'
                                               onclick="go_to_login()"
                                               class='btn btn-primary'/>&nbsp;&nbsp;
                                      </td>
                                    </tr>
                                   </table>
                <div>

                    </div>
                </div>
            </form>
            <br>

              <form method="POST" action="" id="already_account">
              <div class="col">
              <div class="col-md-8 col-md-offset-3">
                                <input type='hidden' name='admin_login' id='admin_login' value='Already have an account?' class='btn btn-primary'/>
                                </div>
               </form>
        </div>
    </div>
</section>
<!-- Essential javascripts for application to work-->
<script src="?resource=/includes/js/jquery-3.2.1.min.js">
</script>
<script src="?resource=/includes/js/popper.min.js">
</script>
<script src="?resource=/includes/js/bootstrap.min.js">
</script>
<script src="?resource=/includes/js/main.js">
</script>
<!-- The javascript plugin to display page loading on top-->
<script src="?resource=/includes/js/plugins/pace.min.js">
</script>
<script>
    var password = document.getElementById("password")
        , confirm_password = document.getElementById("confirm_password");

    function validatePassword() {
        if (password.value != confirm_password.value) {
            confirm_password.setCustomValidity("Passwords Don't Match");
        } else {
            confirm_password.setCustomValidity("");
        }
    }

    password.onchange = validatePassword;
    confirm_password.onkeyup = validatePassword;
</script>
<script>
    function go_to_login(){
    document.getElementById("already_account").submit();
}
</script>
</body>
</html>
