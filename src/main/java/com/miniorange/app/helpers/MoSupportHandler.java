package com.miniorange.app.helpers;

import com.google.gson.JsonObject;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.opensaml.xml.signature.J;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


public class MoSupportHandler extends HttpServlet {
    private static MoSAMLSettings settings;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String AUTH_BASE_URL = "https://login.xecurify.com/moas";
//    public static final String AUTH_BASE_URL = "https://test.miniorange.in/moas";

    public  static  final  String REGISTER_MAIL_URL = AUTH_BASE_URL + "/api/notify/send";
    public static final String SUPPORT_QUERY_URL = AUTH_BASE_URL + "/rest/customer/contact-us";
    public static final String CREATE_CUSTOMER_URL = AUTH_BASE_URL + "/rest/customer/add";
    public static final String CUSTOMER_KEY_URL = AUTH_BASE_URL + "/rest/customer/key";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String DEFAULT_CUSTOMER_KEY = "16555";
    public static final String DEFAULT_API_KEY = "fFd2XcvTGDemZvbw1bcUesNJWEqKbbUq";

    public static String email;
    public static String mobileNumber;
    public static String message;

    public MoSupportHandler() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        doPost(resp, req);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doGet(req, resp);
        doPost(resp, req);
    }

    public static void doPost(HttpServletResponse response, HttpServletRequest request) throws IOException {

//		email = request.getParameter("mo_saml_contact_us_email");
//		mobileNumber = request.getParameter("mo_saml_contact_us_phone");
//		message = request.getParameter("mo_saml_contact_us_query");
//		String result = submitSupportQuery(email, mobileNumber, message);

        // response.sendRedirect("support.jsp");
        // response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		out.print(result);

        /*
         * String spBaseURL =
         * request.getRequestURL().toString().replace(request.getRequestURI(),"");
         * spBaseURL = spBaseURL + "/admin/settings?category=miniorangesaml";
         *
         * out.
         * print("<html><head><title>Submit Query</title></head><body><script type='text/javascript'> alert(\""
         * +result+"\");" +"window.location.href =\""+spBaseURL+"\";"
         * +"</script></body></html>");
         */

    }

    private static HashMap<String, String> getAuthorizationHeaders(Long customerId, String apiKey) {
        HashMap<String, String> headers = new HashMap<String, String>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String stringToHash = customerId + timestamp + apiKey;
        String hashValue = DigestUtils.sha512Hex(stringToHash);

        headers.put("Customer-Key", String.valueOf(customerId));
        headers.put("Timestamp", timestamp);
        headers.put("Authorization", hashValue);
        return headers;
    }

    public static String submitSupportQuery(String email, String phone, String query, Properties p) {
        String response = "";
        settings = new MoSAMLSettings(p);
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("company", settings.getSpBaseUrl());
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("phone", phone);
            jsonObject.addProperty("ccEmail", "javasupport@xecurify.com");
            jsonObject.addProperty("query", StringUtils.prependIfMissing(query, "[JAVA SAML Connector free] "));
            String json = jsonObject.toString();
            response = MoSupportUtils.sendPostRequest(SUPPORT_QUERY_URL, json, CONTENT_TYPE_JSON,
                    getAuthorizationHeaders(Long.valueOf(DEFAULT_CUSTOMER_KEY), DEFAULT_API_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String registermail(StringBuffer content ) {
//        , String purpose, String companyName
        String response = "";
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject mail = new JsonObject();

            mail.addProperty("customerKey",DEFAULT_CUSTOMER_KEY);
            mail.addProperty("bccEmail","javasupport@xecurify.com");
            mail.addProperty("fromName", "miniOrange");
            mail.addProperty("toEmail","javasupport@xecurify.com");
            mail.addProperty("toName","javasupport@xecurify.com");
            mail.addProperty("subject","JAVA Connector free customer Registration");
            mail.addProperty("content",content.toString());

            jsonObject.addProperty("customerKey",DEFAULT_CUSTOMER_KEY);
            jsonObject.addProperty("sendEmail",Boolean.TRUE);
            jsonObject.add("email",mail);
            String json = jsonObject.toString();

            response = MoSupportUtils.sendPostRequest(REGISTER_MAIL_URL, json, CONTENT_TYPE_JSON,
                    getAuthorizationHeaders(Long.valueOf(DEFAULT_CUSTOMER_KEY), DEFAULT_API_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String getCustomerKey(String email, String password) {
        String response = "";

        try {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("password", password);
            String json = jsonObject.toString();
            response = MoSupportUtils.sendPostRequest(
                    CUSTOMER_KEY_URL, json,
                    CONTENT_TYPE_JSON,
                    getAuthorizationHeaders(Long.valueOf(DEFAULT_CUSTOMER_KEY), DEFAULT_API_KEY));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String createCustomer(String email, String password, String companyName) {
        String response = "";



        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("charset", "UTF - 8");
        headers.put("Authorization", "Basic");

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("companyName", companyName);
            jsonObject.addProperty("areaOfInterest", "JAVA SAML Connector");
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("password", password);

            String json = jsonObject.toString();

            response = MoSupportUtils.sendPostRequest(CREATE_CUSTOMER_URL, json, CONTENT_TYPE_JSON, headers);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static boolean isCustomerRegisteredLocal(Properties p) {
        String email = p.getProperty("email");
        String customerKey = p.getProperty("customerKey");

        if (email != null && customerKey != null) {
            return true;
        }
        return false;
    }

}
