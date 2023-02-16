<#import "topbar.ftl" as topbar>
<#import "sidemenu.ftl" as sidemenu>
<#import "script.ftl" as script>
<@topbar.topbar admin_email="${admin_email}"  />
<@sidemenu.sidemenu plugin_setting='' how_to_setup='' licensing='' support='active'/>
<main class='app-content'>
    <#if showMsg>

    <#if !isErrorMsg>
   <div class="alert alert-success alert-dismissible fade show" role="alert">
        <#else>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            </#if>
          ${msg}
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <!-- showMsg = false; -->
        </#if>
        <p name='saml_message'>
        </p>
        <div class='row'>
            <div class='col-md-12'>
                <div class='tile'>
                    <form method='post' action=''>
                        <div class='row'>
                            <div class='col-lg-10'>
                                <p>
                                    <b>Need any help? We can help you in configuring the connector with your Identity
                                        Provider. Just send us a query and we will get back to you soon.
                                    </b>
                                </p>
                                <input type='hidden' name='choice' value='mo_saml_contact_us_query_option'/>
                                <div class='form-group'>
                                    <input class='form-control' type='text' name='mo_saml_contact_us_email'
                                           placeholder='Enter your email' value=''>
                                </div>
                                <div class='form-group'>
                                    <input class='form-control' type='tel' name='mo_saml_contact_us_phone'
                                           pattern='[\+]\d{11,14}|[\+]\d{1,4}[\s]\d{9,10}'
                                           placeholder='Enter your phone number'>
                                </div>
                                <div class='form-group'>
                    <textarea class='form-control' name='mo_saml_contact_us_query' placeholder='Enter your query here'
                              onkeydown='mo_saml_valid_query(this)' onkeyup='mo_saml_valid_query(this)'
                              onblur='mo_saml_valid_query(this)'>
                    </textarea>
                                </div>
                            </div>
                        </div>
                        <div class='tile-footer'>
                            <button class='btn btn-primary' type='submit' name='submit'>Submit
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
<script>
    function mo_saml_valid_query(f) {
        !(/^[a-zA-Z?,.\\(\\)\\/@ 0-9]*$/).test(f.value) ? f.value = f.value.replace(
            /[^a-zA-Z?,.\\(\\)\\/@ 0-9]/, '') : null;
    }
</script>
<@script.script />
