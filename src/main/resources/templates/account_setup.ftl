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
    <style>
        .mo_saml_settings_table {
            width: 100%;
        }

        .mo_saml_settings_table tr td:first-child {
            width: 30%;
        }

        .mo_saml_table_layout {
            background-color: #FFFFFF;
            border: 1px solid #CCCCCC;
            padding: 15px 15px 15px 15px;
            margin-bottom: 10px;
        }

        .mo_saml_table_layout td {
        }

        .mo_saml_table_layout input[type='text'] {
            width: 80%;
        }

        .mo_saml_table_layout td strong {
            margin-left: 10px;
        }
    </style>
</head>
<body class='app sidebar-mini rtl'>

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
<@sidemenu.sidemenu plugin_setting='' how_to_setup='' account_setup='active' licensing='' support=''/>
<main class='app-content'>
    <#if showMsg>

    <#if isErrorMsg>
    <div class='row' style='background: #5bf5a3;border: darkgreen 2px solid;margin: auto;margin-bottom: 20px;'>
        <#else>
        <div class='row' style='background: #ff6666; border: darkred 2px solid;margin: auto;margin-bottom: 20px;'>
            </#if>

            <div style='font-size: 17px; padding: 8px;padding-left: 17px;'>
                ${msg}
            </div>
        </div>
        </#if>
        <div class='row'>
            <div class='col-md-12'>
                <div class='tile'>
                    <div class='row'>
                        <div class='col-lg-12'>
                            <#if p>
                                <div class='mo_saml_table_layout'>
                                    <h2>Thank you for registering with miniOrange.
                                    </h2>
                                    <table border='1'
                                           ;style='background-color:#FFFFFF; border:1px solid #CCCCCC; border-collapse: collapse; padding:0px 0px 0px 10px; margin:2px; width:85%'>
                                        <tr>
                                            <td style='width:45%; padding: 10px;'>miniOrange Account Email
                                            </td>
                                            <td style='width:55%; padding: 10px;'>${email}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style='width:45%; padding: 10px;'>Customer ID
                                            </td>
                                            <td style='width:55%; padding: 10px;'>${customerKey}
                                            </td>
                                        </tr>
                                    </table>
                                    <br/>
                                    <br/>
                                    <table>
                                        <tr>
                                            <td>
                                                <form name='f1' method='post' action='' id='mo_saml_goto_login_form'>
                                                    <input type='hidden' value='change_miniorange_account'
                                                           name='option'/>
                                                    <input type='submit' value='Login / Register with different Account'
                                                           class='btn btn-primary'/>
                                                </form>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            <#else >
                                <#if !alreadyHaveAnAccount>
                                    <form name='f' method='post' action=''>
                                        <input type='hidden' name='option' value='mo_saml_register_customer'/>
                                        <div class='mo_saml_table_layout' id='registration_div'>
                                            <h4>Register with miniOrange
                                            </h4>
                                            <br/>
                                            <h6>Why should I register?
                                            </h6>
                                            <div style='background: aliceblue; padding: 10px 10px 10px 10px; border-radius: 10px;'>
                                                You should register so that in case you need help, we can help you with
                                                step by step
                                                instructions. We support all known IdPs - ADFS, Okta, Salesforce,
                                                Shibboleth,
                                                SimpleSAMLphp, OpenAM, Centrify, Ping, RSA, IBM, Oracle, OneLogin,
                                                Bitium, WSO2 etc.
                                                <b>You will also need a miniOrange account to upgrade to the premium
                                                    version of the connector.
                                                </b> We do not store any information except the email that you will use
                                                to register with us.
                                            </div>
                                            <br/>
                                            <div class='col-lg-8'>
                                                <table class='mo_saml_settings_table'>
                                                    <tr>
                                                        <td>
                                                            <b>
                                                                <font color='#FF0000'>*
                                                                </font>Email:
                                                            </b>
                                                        </td>
                                                        <td>
                                                            <input class='form-control' type='email' name='email'
                                                                   required placeholder='person@example.com'/>
                                                        </td>
                                                    </tr>
                                                    <tr>&nbsp;
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <b>
                                                                <font color='#FF0000'>*
                                                                </font>Password:
                                                            </b>
                                                        </td>
                                                        <td>
                                                            <input class='form-control' required type='password'
                                                                   name='password'
                                                                   placeholder='Choose your password (Min. length 6)'
                                                                   minlength='6' pattern='^[(\\w)*(!@#$.%^&*-_)*]$'
                                                                   title='Minimum 6 characters should be present. Maximum 15 characters should be present. Only following symbols (!@#.$%^&*) should be present.'
                                                            />
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <b>
                                                                <font color='#FF0000'>*
                                                                </font>Confirm Password:
                                                            </b>
                                                        </td>
                                                        <td>
                                                            <input class='form-control' required type='password'
                                                                   name='confirmPassword'
                                                                   placeholder='Confirm your password'
                                                                   minlength='6' pattern='^[(\\w)*(!@#$.%^&*-_)*]$'
                                                                   title='Minimum 6 characters should be present. Maximum 15 characters should be present. Only following symbols (!@#.$%^&*) should be present.'
                                                            />
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;
                                                        </td>
                                                        <td>
                                                            <br>
                                                            <input type='submit' name='submit' value='Register'
                                                                   id='register_action'
                                                                   class='btn btn-primary'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                            <input type='button' name='mo_saml_goto_login'
                                                                   id='mo_saml_goto_login'
                                                                   value='Already have an account?'
                                                                   class='btn btn-primary'/>&nbsp;&nbsp;
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </form>
                                    <form name='f1' method='post' action='' id='mo_saml_goto_login_form'>
                                        <input type='hidden' name='option' value='mo_saml_goto_login'/>
                                    </form>
                                <#else>
                                    <form name='f' method='post' action=''>
                                        <input type='hidden' name='option' value='mo_saml_verify_customer'/>
                                        <div class='mo_saml_table_layout'>
                                            <div id='toggle1' class='panel_toggle'>
                                                <h3>Login with miniOrange Account
                                                </h3>
                                            </div>
                                            <div id='panel1'>
                                                <p>
                                                    <b>Please enter your miniOrange email
                                                        and password.
                                                        <br/>
                                                        <a target='_blank'
                                                           href='https://login.xecurify.com/moas/idp/resetpassword'>Click
                                                            here if you forgot your password?
                                                        </a>
                                                    </b>
                                                </p>
                                                <br/>
                                                <div class='col-lg-8'>
                                                    <table class='mo_saml_settings_table'>
                                                        <tr>
                                                            <td>
                                                                <b>
                                                                    <font color='#FF0000'>*
                                                                    </font>Email:
                                                                </b>
                                                            </td>
                                                            <td>
                                                                <input class='form-control' type='email' name='email'
                                                                       required placeholder='person@example.com'/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <b>
                                                                    <font color='#FF0000'>*
                                                                    </font>Password:
                                                                </b>
                                                            </td>
                                                            <td>
                                                                <input class='form-control' required type='password'
                                                                       name='password' placeholder='Enter your password'
                                                                       minlength='6' pattern='^[(\\w)*(!@#$.%^&*-_)*]$'
                                                                       title='Minimum 6 characters should be present. Maximum 15 characters should be present. Only following symbols (!@#.$%^&*) should be present.'
                                                                />
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>&nbsp;
                                                            </td>
                                                            <td>
                                                                <input type='submit' name='submit' value='Login'
                                                                       class='btn btn-primary'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </#if>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<@script.script />
