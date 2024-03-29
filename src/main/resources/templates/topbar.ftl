<#macro topbar admin_email>
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




    </head>
<body class='app sidebar-mini rtl'>
<!-- Navbar-->
<header class='app-header'><a class='app-header__logo' href='#' style='margin-top:10px;'><img
                src='?resource=/resources/images/logo-home.png'></a>
    <!-- Sidebar toggle button<a class='app-sidebar__toggle' href='#' data-toggle='sidebar' aria-label='Hide Sidebar'></a> -->
    <ul class='app-nav'>
        <li class='dropdown'><a class='app-nav__item' href='#' data-toggle='dropdown' aria-label='Open Profile Menu'><i
                        class='fa fa-user fa-lg'><span style='margin-left:5px'>${admin_email}</span><span
                            style='padding-left:5px;'><i class='fa fa-caret-down'></i></span></i></a>
            <ul class='dropdown-menu settings-menu dropdown-menu-right'>
                <li><a class='dropdown-item' href='?action=logout'><i class='fa fa-sign-out fa-lg'></i> Logout</a></li>
            </ul>
        </li>
    </ul>
</header>


</#macro>