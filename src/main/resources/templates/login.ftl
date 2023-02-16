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
    <title>Login - miniOrange Admin</title>
</head>
<body>
<section class="material-half-bg">
    <div class="cover"></div>
</section>
<section class="login-content">
    <div class="logo">
        <h1><img src="?resource=/resources/images/logo_large.png"></h1>
    </div>
    <div class="col-md-6">
        <div class="tile">
            <h3 class="tile-title">Login with Miniorange</h3>
            <form class="login_form" method="POST" action="">
                <input type="hidden" name="choice" value="login">
                <input type="hidden" name="login_without_register" value="login">

                <div class="tile-body">
                    <div class="form-group row">
                        <label class="control-label col-md-3">Email</label>
                        <div class="col-md-8">
                            <input class="form-control col-md-8" type="email" name="email"
                                   placeholder="Enter email address" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="control-label col-md-3">Password</label>
                        <div class="col-md-8">
                            <input class="form-control col-md-8" type="password" name="password" id="password"
                                   placeholder="Enter a password" minlength="6" required>
                        </div>
                    </div>

                    <#if admin_login?has_content>
                    <div class="form-group row">
                        <label class="control-label col-md-3">Use Case</label>
                        <div class="col-md-8">
                            <input class="form-control col-md-8" type="text" name="use_case" id="use_case"
                                   placeholder="Enter a your Use Case" minlength="6">
                        </div>
                    </div>
                    </#if>


                </div>
                <div class="tile-footer">
                    <div class="row">
                        <div class="col-md-8 col-md-offset-3">
                            <button class="btn btn-primary" type="submit"><i class="fa fa-fw fa-lg fa-check-circle"></i>Login
                            </button>
                        </div>
                    </div>

                    <#if admin_login?has_content>
                    <a href="#" onclick="history.back()">Back to Registration Page !</a>
                    </#if>
                </div>
            </form>
        </div>
    </div>
</section>


<!-- Essential javascripts for application to work-->
<script src="?resource=/includes/js/jquery-3.2.1.min.js"></script>
<script src="?resource=/includes/js/popper.min.js"></script>
<script src="?resource=/includes/js/bootstrap.min.js"></script>
<script src="?resource=/includes/js/main.js"></script>
<!-- The javascript plugin to display page loading on top-->
<script src="?resource=/includes/js/plugins/pace.min.js"></script>
<script type="text/javascript" src="?resource=/includes/js/plugins/bootstrap-notify.min.js"></script>
<script type="text/javascript" src="?resource=/includes/js/plugins/sweetalert.min.js"></script>

<#if invalid_credentials?? >
    <#if invalid_credentials>
        <script>
            $(document).ready(function () {
                $.notify({
                    title: "ERROR : ",
                    message: "Invalid username or password",
                    icon: "fa fa-times"
                }, {
                    type: "danger"
                });
            });
        </script>
    </#if>
</#if>
</body>
</html>