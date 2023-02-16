jQuery(document).ready(function(){
    jQuery("#signing_info").click(function(){
        jQuery("#signing_help_desc").slideToggle(400);
    });

    jQuery("#application_url_help").click(function(){
        jQuery("#application_url_help_desc").slideToggle(400);
    });

    jQuery("#sp_certificate_help").click(function(){
        jQuery("#sp_certificate_help_desc").slideToggle(400);
    });

    $("#download_sp_cert").click(function(e){
        e.preventDefault();
        window.location.href = 'resources/sp-certificate.crt';
    });

    jQuery("#logout_url_help").click(function(){
        jQuery("#logout_url_help_desc").slideToggle(400);
    });
    
    jQuery("#mo_saml_goto_login").click(function () {
        jQuery("#mo_saml_goto_login_form").submit();
    });
});

$(function() {
    // Remove button click
    $(document).on(
        'click',
        '[data-role="dynamic-fields"] > .form-inline [data-role="remove"]',
        function(e) {
            e.preventDefault();
            $(this).closest('.form-inline').remove();
        }
    );
    // Add button click
    $(document).on(
        'click',
        '[data-role="dynamic-fields"] > .form-inline [data-role="add"]',
        function(e) {
            e.preventDefault();
            var container = $(this).closest('[data-role="dynamic-fields"]');
            new_field_group = container.children().filter('.form-inline:first-child').clone();
            new_field_group.find('input').each(function(){
                $(this).val('');
            });
            container.append(new_field_group);
        }
    );
});
//function enableTestingButton(){
//if(document.getElementById('saml_form').submit){
//document.getElementById('saml_form').disabled = false
//}
//}

$(function () {
    $(document).ready(function () {
        var clicked = sessionStorage.getItem('clicked');
        if (clicked == 'true') {
            $('#testing').removeAttr('disabled');
        } else {
            $('#testing').attr('disabled', 'disabled');
        }
    });
    $('#submit').on("click", function () {
        sessionStorage.setItem('clicked', 'true');
    });
    $('#reset').on("click", function () {
        sessionStorage.setItem('clicked', 'false');
    });
});

$(".alert").delay(4000).slideUp(200, function() {
    $(this).alert('close');
});



