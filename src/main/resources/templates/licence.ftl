<#import "sidemenu.ftl" as sidemenu>
<#import "script.ftl" as script>
<!DOCTYPE html>
<html lang='en'>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <!-- Main CSS-->
    <link rel='stylesheet' type='text/css' href='?resource=/includes/css/main.css'>
    <!-- Font-icon css-->
    <link rel='stylesheet' type='text/css'
          href='https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css'>
    <link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.6.3/css/all.css'>
    <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js'>
    </script>
    <style>
        .contactus-link {
            font-weight: bold;
        }

        .medium-font-size {
            font-size: medium;
        }

        .upgrade-now:hover {
            color: #fff;
            font-weight: 600;
        }

        .upgrade-now-link:hover {
            color: #fff !important;
            font-weight: 600;
        }

        .select-design {
            border: 1px solid #ddd;
            background-color: #fff;
            color: #32373c;
            background-color: white;
            border-radius: 4px;
            min-height: auto;
        }

        .upgrade-now-link:hover {
            opacity: 0.5;
            cursor: pointer;
        }

        .center-pills {
            display: inline-block;
        }

        .nav-pills {
            border: 1px solid #2f6062;
            height: 53px;
            border-radius: 50em;
            padding-top: 2px;
            padding-left: 0.2em;
            padding-right: 0.2em;
        }

        .nav-pills > li {
            width: 200px;
        }

        .tab-font {
            vertical-align: text-bottom;
            font-size: 15px;
        }

        .nav-pills > li + li {
            margin-left: 0px;
        }

        .nav-pills > li.active > a, .nav-pills > li.active > a:hover, .nav-pills > li.active > a:focus, .nav-pills > li.active > a:active {
            color: #fff;
            background-color: #3f8588;
            height: 47px;
            border-radius: 50em;
        }

        .nav-pills > li > a:hover {
            color: #fff;
            background: #44cec1;
            height: 46px;
            border-radius: 50em;
        }

        .nav-pills > li > a:focus {
            color: #fff;
            background: grey;
            height: 47px;
            border-radius: 50em;
        }

        .nav-pills > li.active {
            background-color: #2f6062;
            border-radius: 50em;
        }

        .thumbnail {
            border: 1px solid #898080;
            border-radius: 4px;
            -webkit-transition: border .2s ease-in-out;
            -o-transition: border .2s ease-in-out;
            transition: border .2s ease-in-out;
            background: #8B7979 !important;
            width: 300px;
            text-align: center;
            padding-right: 20px !important;
        }

        .img-size {
            max-width: 60%;
            margin-left: 20%;
            border: 1px solid black;
        }

        .mo-tab {
            padding: 0px !important;
            margin-left: -150px !important;
            color: #fff !important;
        }

        .mo-tabgt
        p {
            text-align: center;
            font-size: 16px;
        }

        .btn-large {
            padding: 5px !important;
            font-size: 20px;
        }

        .mo-tabgt
        h3 {
            margin-top: 0% !important;
        }

        .nav-pills > li > a {
            border-radius: 0px;
            height: 47px;
            border-color: #E85700;
            font-weight: 500;
            color: #101110;
            text-transform: uppercase;
        }

        .contactus-link a:hover {
            color: #00a0d2;
        }

        .contactus-link a {
            color: #0073aa;
            font-weight: 600;
        }

        .upgrade-now-dropbtn {
            background-color: #000;
            color: white;
            padding: 16px;
            font-size: 16px;
            border: none;
            cursor: pointer;
        }

        .upgrade-now-dropdown {
            position: relative;
            display: inline-block;
        }

        .dropdown-content {
            display: none;
            position: absolute;
            background-color: #f9f9f9;
            min-width: 160px;
            box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
            z-index: 1;
        }

        .dropdown-menu-center {
            left: 50% !important;
            right: auto !important;
            text-align: center !important;
            transform: translate(-50%, 0) !important;
            width: 100%;
        }

        .dropdown-content a {
            color: black;
            padding: 12px 16px;
            text-decoration: none;
            display: block;
        }

        .price li:nth-of-type(2n+1) {
            background-color: rgba(23, 61, 80, 0.06);
        }

        .price1 li:nth-of-type(2n) {
            background-color: rgba(23, 61, 80, 0.06);
        }

        .wp-sso .popover {
            background-color: #fff !important;
            color: #fff;
            font-size: 1.5em;
        }

        .columns {
            float: left;
            width: 45.3%;
            padding: 9px;
        }

        .price {
            list-style-type: none;
            border: 2px solid #eee;
            margin: 0;
            padding: 0;
            -webkit-transition: 0.3s;
            transition: 0.3s;
        }

        .price:hover {
            box-shadow: 0 8px 12px 0 rgba(0, 0, 0, 0.2)
        }

        .price li {
            border-bottom: 1px solid #eee;
            text-align: center;
            padding: 15px;
        }

        h4 {
            font-size: 15px;
        }

        .wp-sso .standard-enterprise h2 {
            color: #2f6062;
            font-weight: 700;
            font-size: 1.7em;
            margin-bottom: 0em;
        }

        .wp-sso .premium h2 {
            color: #44cec1;
            font-weight: 700;
            font-size: 1.7em;
            margin-bottom: 0em;
        }

        .wp-sso h3 {
            color: #000;
            font-weight: 500;
            font-size: 1.2em;
            padding-bottom: 1em;
        }

        .wp-sso .standard-enterprise .cd-value {
            color: #2f6062;
            font-weight: 200;
            font-size: 2.5em;
        }

        .wp-sso .premium .cd-value {
            color: #44cec1;
            font-weight: 200;
            font-size: 2.5em;
        }

        .wp-sso .cd-currency {
            font-weight: 500;
            color: #1a445866;
            font-size: 1.5em;
        }

        .wp-sso sup {
            top: -0.79em;
        }

        .wp-sso ul {
            list-style-type: none;
        }

        .wp-sso .header {
            background: #f2f5f8;
            margin: 0px;
            list-style-type: none;
            padding: 2em;
        }

        .wp-sso .free-version-li {
            display: block;
            margin: 0px;
            text-transform: uppercase;
            padding: 0em;
            background: #44CEC1;
        }

        .wp-sso .free-version {
            display: block;
            padding: 1.28em 1em 1.28em 1em;
            color: #fff;
        }

        .wp-sso .upgrade-now {
            display: block;
            margin: 0px;
            text-transform: uppercase;
            padding: 0em;
            background: #000;
        }

        .wp-sso .upgrade-now-link {
            display: block;
            padding: 2em 1em 2em 1em;
            color: #fff;
        }

        .wp-sso ul li {
            margin: 0px;
            list-style: none;
            font-size: 0.9em;
            padding: 0.8em;
            font-weight: 500;
        }

        .wp-sso .bold-features {
            padding: 0.5em;
            font-size: 0.8em;
            color: #0bb3a3;
            font-weight: 700;
        }

        .wp-sso {
            margin-top: 2em;
        }

        .wp-sso ul {
            padding: 0em;
            margin-left: 0px;
        }

        .wp-sso .contactus-margin {
            margin-bottom: 2.4em;
        }

        #dollar {
            top: -1.79em;
        }
    </style>
    <script>
        function upgradeform(planType) {
            $('#requestOrigin').val(planType);
            $('#loginform').submit();
        }
    </script>
</head>
<body class='app sidebar-mini rtl'>
<form style='display:none;' id='loginform' action='https://login.xecurify.com/moas/login' target='_blank' method='post'>
    <input type='text' name='redirectUrl' value='https://login.xecurify.com/moas/initializepayment'/>
    <input type='text' name='requestOrigin' id='requestOrigin'/>
</form>
<!-- Navbar-->
<header class='app-header'>
    <a class='app-header__logo' href='#' style='margin-top:10px;'>
        <img src='?resource=/resources/images/logo-home.png'>
    </a>
    <!-- Sidebar toggle button -->
    <ul class='app-nav'>
        <li class='dropdown'>
            <a class='app-nav__item' href='#' data-toggle='dropdown' aria-label='Open Profile Menu'>
                <i class='fa fa-user fa-lg'>
                  <span style='margin-left:5px'>${admin_email}
                  </span>
                    <span style='padding-left:5px;'>
                    <i class='fa fa-caret-down'>
                    </i>
                  </span>
                </i>
            </a>
            <ul class='dropdown-menu settings-menu dropdown-menu-right'>
                <li>
                    <a class='dropdown-item' href='?action=logout'>
                        <i class='fa fa-sign-out fa-lg'>
                        </i>Logout
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</header>
<!-- Sidebar menu-->
<@sidemenu.sidemenu plugin_setting='' how_to_setup='' licensing='active' support=''/>
<main class='app-content'>
    <!-- <div class='app-title'>
<div>
<h1><i class='fa fa-dollar'></i>  Licensing</h1>
</div>
<ul class='app-breadcrumb breadcrumb'>
<li class='breadcrumb-item'><i class='fa fa-home fa-lg'></i></li>
<li class='breadcrumb-item'><a href='#'>Licensing</a></li>
</ul>
</div>  -->
    <p name='saml_message'>
    </p>
    <div class='row'>
        <div class='col-md-12'>
            <div class='tile'>
                <div class='row'>
                    <div class='col-lg-10'>
                        <input type='hidden' value='false' id='mo_customer_registered'>
                        <div id='pricing_container' style='text-align:center; padding-bottom:30px;'>
                            <h3 style='margin-left:180px'>
                                <b>Licensing Plans:
                                </b>
                            </h3>
                            <br>
                            <div>
                                <a id='pricing-table'>
                                </a>
                                <div class='container'>
                                    <div class='container'
                                         style='margin-left:8em;width: 104% !important;font-family: open sans;font-size:14px'>
                                        <div class='columns wp-sso'>
                                            <ul class='price'>
                                                <li class='header premium' style='padding-bottom:3.8em;'>
                                                    <h2>FREE
                                                    </h2>
                                                    <br>
                                                    <sup id='dollar'>
                                    <span class='cd-currency'>$
                                    </span>
                                                    </sup>
                                                    <span class='cd-value'>0
                                  </span>
                                                </li>
                                                <li class='free-version-li'>
                                  <span class='free-version'>You are using the free version of Java SAML Connector
                                  </span>
                                                </li>
                                                <li class='bold-features' style='color:#2f6062;'>
                                                    <strong>See the Free Plugin features list below
                                                    </strong>
                                                    <br>
                                                    <br>
                                                </li>
                                                <li class='medium-font-size'>Unlimited Authentications
                                                </li>
                                                <li class='medium-font-size'>Configurable SP Base URL
                                                </li>
                                                <li class='medium-font-size'>Custom Application URL
                                                </li>
                                                <li class='medium-font-size'>Add Login link in your application
                                                </li>
                                                <li class='medium-font-size'>Attribute mapping (Only NameID is allowed)
                                                </li>
                                                <li>-
                                                </li>
                                                <li>-
                                                </li>
                                                <li>-
                                                </li>
                                                <li>-
                                                </li>
                                                <li>-
                                                </li>
                                                <li>
                                                    &nbsp;
                                                    <b>Support
                                                    </b>
                                                    <p class='contactus-margin'>
                                                        <a href='https://www.miniorange.com/contact' target='_blank'
                                                           class='contactus-link'>Contact Us
                                                        </a>
                                                    </p>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class='columns wp-sso'>
                                            <ul class='price'>
                                                <li class='header standard-enterprise' style='padding-bottom:3.8em;'>
                                                    <h2>PREMIUM
                                                    </h2>
                                                    <br>
                                                    <div class='cd-price'>
                                                        <sup id='dollar'>
                                      <span class='cd-currency'>$
                                      </span>
                                                        </sup>
                                                        <span class='cd-value'>449
                                      <sup>
                                        <span>*
                                        </span>
                                      </sup>
                                    </span>
                                                    </div>
                                                </li>
                                                <li class='upgrade-now'>
                                                    <a class='upgrade-now-link'
                                                       onclick="upgradeform('java_saml_premium_plan')"
                                                       style='color:#fff;'>Upgrade Now
                                                    </a>
                                                </li>
                                                <li class='bold-features' style='color:#2f6062;'>
                                                    <strong>See the Premium Plugin features list below
                                                    </strong>
                                                    <br>
                                                    <br>
                                                </li>
                                                <li class='medium-font-size'>Unlimited Authentications
                                                </li>
                                                <li class='medium-font-size'>Signed Request
                                                </li>
                                                <li class='medium-font-size'>Signed Assertion and Response
                                                </li>
                                                <li class='medium-font-size'>Configurable SAML request binding type
                                                </li>
                                                <li class='medium-font-size'>Custom Attribute mapping
                                                </li>
                                                <li class='medium-font-size'>SAML Single Logout
                                                </li>
                                                <li class='medium-font-size'>Force Authentication
                                                </li>
                                                <li class='medium-font-size'>Custom Application URL
                                                </li>
                                                <li class='medium-font-size'>Configurable SP Base URL
                                                </li>
                                                <li class='medium-font-size'>Add Login link in your application
                                                </li>
                                                <li>
                                                    &nbsp;
                                                    <b>Support
                                                    </b>
                                                    <p class='contactus-margin'>
                                                        <a href='https://www.miniorange.com/contact' target='_blank'
                                                           class='contactus-link'>Contact Us
                                                        </a>
                                                    </p>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>
</main>
<@script.script />
