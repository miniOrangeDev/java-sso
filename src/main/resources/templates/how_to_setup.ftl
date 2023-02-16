<#import "topbar.ftl" as topbar>
<#import "sidemenu.ftl" as sidemenu>
<#import "script.ftl" as script>
<@topbar.topbar admin_email="${admin_email}"  />
<@sidemenu.sidemenu plugin_setting='' how_to_setup='active' licensing='' support=''/>
<main class='app-content'>
    <#if submit??>
        <div class='row' style='background: #5bf5a3;border: darkgreen 2px solid;margin: auto; margin-bottom: 20px;'>
            <div style='font-size: 17px; padding:8px; padding-left: 17px;'>Redirect endpoint has been saved.</div>
        </div>
    </#if>

    <div class='row'>
        <div class='col-md-12'>
            <div class='tile'>
                <div class='row'>
                    <div class='col-lg-10'>

                        <h3>Follow these steps to setup the plugin:</h3>
                        <h4>Step 1:</h4>
                        <ul>
                            <li>You can configure the <b>SP Base URL</b> or leave this option as it is.</li>
                            <li>You need to provide these <b>SP Entity ID</b> and <b>ACS URL</b> values while
                                configuring your Identity Provider
                            </li>
                        </ul>
                        <img src='?resource=/resources/images/java_saml_connector_sp_settings_1.png'
                             style='width:800px;height:380px;margin-left:50px; border:1px solid;'>
                        <br/><br/>
                        <h4>Step 2:</h4>
                        <ul>
                            <li>Use your Identity Provider details to configure the plugin.</li>
                            <li>Click on the <b>Save</b> button to save your configuration.</li>
                        </ul>
                        <img src='?resource=/resources/images/java_saml_connector_idp_settings_1.png'
                             style='width:800px;height:380px;margin-left:50px; border:1px solid;'>
                        <br/><br/>
                        <h4>Step 3:</h4>
                        <ul>
                            <li>You can test if the plugin is configured properly or not by clicking on the <b>Test
                                    Configuration</b> button.
                            </li>
                        </ul>
                        <img src='?resource=/resources/images/java_saml_connector_test_configuration.png'
                             style='width:800px;height:380px;margin-left:50px; border:1px solid;'>
                        <ul>
                            <br/>
                            <li>If the configuration is correct, you should see a Test Successful screen with the user's
                                attribute values.
                            </li>
                        </ul>
                        <img src='?resource=/resources/images/java_saml_connector_test_result.png'
                             style='width:600px;height:400px;margin-left:50px; border:1px solid;'>
                        <br/><br/>
                        <h4>Step 4:</h4>
                        <p>Use the following URL as a link in your application from where you want to perform SSO:<br/>
                            <code>http://&lt;your-domain&gt;/&lt;application-name&gt;/sso?action=login</code></p>
                        <p>OR<br/>
                        <p>You can use the following URL if you want to redirect user to a specific page after the
                            SSO:<br/>
                            <code>http://&lt;your-domain&gt;/&lt;application-name&gt;/sso?action=login&redirectto=&lt;redirect-url&gt;</code>
                        </p>
                        <p>For example, you can use it as:<br/>
                            <code>&lt;a href='http://&lt;your-domain&gt;/&lt;application-name&gt;/sso?action=login&redirectto=/&lt;application-name&gt;/dashboard'&gt;Log
                                in&lt;/a&gt;</code>
                        <h4>Step 5:</h4>

                        <form id='setup_form' method='POST' action=''>

                            <div class='form-group'>
                                <label><b>Redirect Endpoint</b><span class='badge badge-pill badge-primary float-right'
                                                                     onclick=''
                                                                     title='The users will be redirected to this URL after logging in.'> ?</span>

                                </label>
                                <table style='width: 100%'>
                                    <tr>
                                        <td><input class='form-control' name='applicationUrl' id='applicationUrl'
                                                   type='text' value='${applicationUrl}'></td>
                                        <td style='padding-left:20px'>
                                            <button class='btn btn-primary' type='submit' name='submit' id='submit'>
                                                Submit
                                            </button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </form>
                        <h4>Step 6:</h4>
                        <p>Please provide your Redirect Endpoint in the above field. You can use the following code
                            snippet in your application endpoint to retrieve the session attributes :</p>
                        <figure style='background-color:#f2f2f2; border-radius:5px; border: #bfbfbf solid 1px'>
                           <pre>
                           <code>
                            HttpSession session = request.getSession(false);
                            String email = (String)session.getAttribute('email');
                           </code>
                          </pre>
                        </figure>
                        <p>The variable <code>email</code> will contain the received user attribute.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<@script.script />