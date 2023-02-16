<#import "topbar.ftl" as topbar>
<#import "sidemenu.ftl" as sidemenu>
<#import "script.ftl" as script>
<@topbar.topbar admin_email= "${admin_email}" />
<@sidemenu.sidemenu plugin_setting='active' how_to_setup='' licensing='' support=''/>

<main class='app-content'>
    <#if submit??>

        <div class="alert alert-success alert-dismissible fade show" role="alert">
          Your Configuration has been saved.
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>

    </#if>

    <div class='row'>
        <div class='col-md-12'>
                <div class='tile'>
                    <div class='row'>
                        <div class='col-lg-6'>
                        <h4>Identity Provider Settings</h4>
                        <br>
                        <div class="form-group">
                          <table>
                            <form method='POST' action='' enctype='multipart/form-data'>
                              <input type="hidden" name="option" value="upload_metadata_file">
                              <tr align="right">
                                <td align="left">
                                  <b>Upload Metadata: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
                                </td>
                                <td colspan="2" align="right">
                                  <input type="file" name="metadata_file" width="10%">
                                </td>
                                <td align="left">
                                  <button class='btn btn-primary' type='submit' name='upload' id='upload' style='margin-right:10px;'>Upload</button>
                                </td>
                              </tr>
                            </form>
                          </table>
                        </div>
                         <hr>

            <form method='POST' action='' id='saml_form'>
                <input type='hidden' name='appName' value='appName'>


                            <div class='form-group'>
                                <label for='idpName'><b>Identity Provider Name</b></label>
                                <input required class='form-control' name='idpName' id='idpName' type='text'
                                       value='${idpName}'>
                            </div>
                            <div class='form-group'>
                                <label for='idpEntityId'><b>IDP Entity ID</b></label>
                                <input required class='form-control' name='idpEntityId' id='idpEntityId' type='text'
                                       value='${idpEntityId}'>
                            </div>
                            <div class='form-group'>
                                <label for='samlLoginUrl'><b>SAML Login URL</b></label>
                                <input required class='form-control' name='samlLoginUrl' id='samlLoginUrl' type='text'
                                       value='${samlLoginUrl}'>
                            </div>

                            <label><b>SAML Login Binding type</b></label>
                            <table>
                                <tr>
                                    <td style='padding:10px;'>
                                        <div>
                                            <label>
                                                <input type='radio' name='samlLoginBindingType'
                                                       id='http_redirect_binding' value='HttpRedirect' checked>
                                                <span class='label-text' style='margin-left:5px;'>Http-Redirect</span>
                                            </label>
                                        </div>
                                    </td>
                                    <td style='padding:10px;'>
                                        <fieldset disabled=''>
                                            <div>
                                                <label>
                                                    <input type='radio' name='samlLoginBindingType'
                                                           id='http_post_binding' value='HttpPost'><span
                                                            class='label-text' style='margin-left:5px;'>Http-Post</span>
                                                </label>
                                                <small style='color:#FF0000'>Not available in TRIAL version</small>
                                            </div>
                                        </fieldset>
                                    </td>
                                </tr>
                            </table>

                            <fieldset disabled=''>
                                <div class='form-group'>
                                    <label for='samlLogoutUrl'><b>SAML Logout URL</b></label>
                                    <input class='form-control' name='samlLogoutUrl' id='samlLogoutUrl' type='text'
                                           value=''>
                                    <small style='color:#FF0000'>Not available in TRIAL version</small>
                                </div>
                            </fieldset>
                            <fieldset disabled=''>
                                <div class='form-group'>
                                    <label for='x509Certificate'><b>SAML x509 Certificate</b></label>
                                    <textarea class='form-control' id='x509Certificate' name='x509Certificate' rows='5'>
	                        </textarea>
                                    <small style='color:#FF0000'>Not available in TRIAL version</small>
                                </div>
                            </fieldset>


                            <fieldset disabled=''>
                                <div>
                                    <label>
                                        <input type='checkbox' id='signResponse' name='signResponse'

                                        ><span class='label-text' style='margin-left:5px;'>Signed Response</span>
                                    </label>&nbsp
                                    <small style='color:#FF0000'>Not available in TRIAL version</small>
                                </div>
                            </fieldset>

                            <fieldset disabled=''>
                                <div>
                                    <label>
                                        <input type='checkbox' id='signAssertion' name='signAssertion'

                                        ><span class='label-text' style='margin-left:5px;'>Signed Assertion</span>
                                    </label>&nbsp
                                    <small style='color:#FF0000'>Not available in TRIAL version</small>
                                </div>
                            </fieldset>
                        </div>
                        <div class='col-lg-4 offset-lg-1'>
                            <h4>Service Provider Settings</h4>

                            <br>
                           <table>


                                    <tr align="right">
                                     <td align="left">
                                            <b>Download Metadata: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</b>
                                    </td>
                                    <td align="left"> <a type="button" class="btn btn-primary" href="?resource=/metadata/spmetadata.xml">Download</a></td>
                                    </tr>

                            </table>
                            <hr>
                            <div class='form-group'>
                                <label for='spBaseUrl'><b>Base URL</b></label>
                                <input class='form-control' id='spBaseUrl' name='spBaseUrl' type='text'
                                       value=${baseUrl}>
                            </div>

                            <div class='form-group'>
                                <label for='spEntityId'><b>SP Entity ID</b></label>
                                <input class='form-control' id='spEntityId' name='spEntityId' type='text' readonly=''
                                       value=${entityId}>
                            </div>

                            <div class='form-group'>
                                <label for='acsUrl'><b>ACS URL</b></label>
                                <input class='form-control' id='acsUrl' name='acsUrl' type='text' readonly=''
                                       value=${acs_Url}>
                            </div>
                            <br><br>
                            <h4>Attribute Mapping</h4>
                            <br>
                            <fieldset disabled=''>
                                <div class='form-group'>
                                    <label for='amEmail'><b>EMAIL</b></label>
                                    <input class='form-control' id='amEmail' name='amEmail' type='text' value='NameID'>
                                    <small style='color:#FF0000'>Not configurable in TRIAL version</small>
                                </div>
                            </fieldset>

                            <fieldset disabled=''>
                                <div class='form-group'>
                                    <label for='amUsername'><b>Username</b></label>
                                    <input class='form-control' id='amUsername' name='amUsername' type='text'
                                           value='NameID'>
                                    <small style='color:#FF0000'>Not configurable in TRIAL version</small>
                                </div>
                            </fieldset>


                        </div>
                    </div>
                    <div class='tile-footer'>
                        <input type='hidden' name='clicked' value='true'/>
                        <button class='btn btn-primary' type='submit' name='submit' id='submit'
                                style='margin-right:10px;' onsubmit='enableTestingButton()'>Save
                        </button>
                        <a target='_blank' href='?action=login&RelayState=testconfig' style='text-decoration:none'>
                            <button class='btn btn-primary' name='do_sso' type='button' id='testing' disabled='true'>
                                Test Configuration
                            </button>
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>

</main>

<script>
function enableTestingButton(){
document.getElementById('testing').disabled = false;
}
</script>

<script>
if ( window.history.replaceState ) {
  window.history.replaceState( null, null, window.location.href );
}

</script>

<@script.script />