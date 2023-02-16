<#macro sidemenu plugin_setting how_to_setup licensing support>
    <aside class='app-sidebar'>
        <div class='app-sidebar__user' style='padding-left:40px'><img src='?resource=/resources/images/miniorange.png'
                                                                      style='width:37.25px; height:50px;'
                                                                      alt='User Image'>
            <div style='margin-left:15px;'>
                <p class='app-sidebar__user-name'>JAVA SAML</p>
                <p class='app-sidebar__user-designation'>Connector</p>
            </div>
        </div>
        <ul class='app-menu'>
            <li><a class="app-menu__item" ${plugin_setting} href="?action=setup"><i style="font-size:20px;"
                                                                                    class="app-menu__icon fa fa-gear"></i><span
                            class="app-menu__label"><b>Plugin Settings</b></span></a></li>

            <li><a class="app-menu__item" ${how_to_setup} href="?action=setupguide"><i style="font-size:20px;"
                                                                                       class="app-menu__icon fa fa-info-circle"></i><span
                            class="app-menu__label"><b>How to Setup?</b></span></a></li>

            <li><a class="app-menu__item" ${licensing} href="?action=licence"><i style="font-size:20px;"
                                                                                 class="app-menu__icon fa fa-dollar"></i><span
                            class="app-menu__label"><b>Licensing</b></span></a></li>
            <li><a class="app-menu__item" ${support} href="?action=support"><i style="font-size:20px;"
                                                                               class="app-menu__icon fa fa-support"></i><span
                            class="app-menu__label"><b>Support</b></span></a></li>
        </ul>
    </aside>
</#macro>