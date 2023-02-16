package com.miniorange.app.servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miniorange.app.classes.MoSAMLException;
import com.miniorange.app.classes.MoSAMLResponse;
import com.miniorange.app.classes.MoSAMLTestResult;
import com.miniorange.app.helpers.MoSAMLManager;
import com.miniorange.app.helpers.MoSAMLSettings;
import com.miniorange.app.helpers.MoSupportHandler;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import static com.miniorange.app.helpers.MoSAMLUtils.getMetadataTemplate;


/**
 * Servlet implementation class RegisterServlet
 */
public class SSOServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    MoSAMLSettings settings;
    MoSAMLManager manager;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSOServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String appName = request.getContextPath();

        if (request.getParameterMap().containsKey("action") && request.getParameter("action").equals("logout")) {
            if (session.getAttribute("authorized") != null) {
                session.setAttribute("authorized", null);
                session.setAttribute("admin_login","false");
            }
            response.sendRedirect("sso");
        }
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassLoaderForTemplateLoading(SSOServlet.class.getClassLoader(), "templates");

        if (!isUserRegistered() && !request.getParameterMap().containsKey("resource") && request.getParameter("admin_login")==null && request.getParameter("login_without_register")==null ) {

            if (request.getParameter("choice")!=null) {
                String email = "", password = "";
                String  confirmPassword="";
                String tempPassword="";
                String purpose="";
                if (request.getParameter("purpose") != null) {
                    purpose= request.getParameter("purpose");
                }
                if (request.getParameter("confirmPassword") != null) {
                    confirmPassword = request.getParameter("confirmPassword");
                }
                if (request.getParameter("email") != null) {
                    email = request.getParameter("email");
                }
                if (request.getParameter("password") != null) {
                    password = request.getParameter("password");
                    tempPassword=request.getParameter("password");
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        byte[] messageDigest = md.digest(password.getBytes());
                        BigInteger no = new BigInteger(1, messageDigest);
                        String hashtext = no.toString(16);
                        while (hashtext.length() < 32) {
                            hashtext = "0" + hashtext;
                        }
                        password = hashtext;
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (request.getParameter("choice").equals("register")) {

                    Boolean showMsg = false;
                    Boolean isErrorMsg = false;
                    String msg = "";
                    Properties p = new Properties();
                    String fileName = getServletContext().getRealPath("WEB-INF/credentials.properties");
                    File f = new File(fileName);

                    if (!f.exists()) {
                        f.createNewFile();
                    }

                    String companyName =  request.getRequestURL().toString();

                    if (companyName.contains("/sso")){

                        companyName=companyName.replace("/sso", "");
                    }


                    if (tempPassword.equals(confirmPassword)) {
                        String moResponse = MoSupportHandler.createCustomer(email, confirmPassword,companyName);
                        try {
                            JsonObject jsonObject = new JsonParser().parse(moResponse).getAsJsonObject();
                            if (jsonObject.get("status").getAsString().equals("CUSTOMER_USERNAME_ALREADY_EXISTS")) {
                                String moResponseforlogin = MoSupportHandler.getCustomerKey(email, confirmPassword);
                                try {
                                    JsonObject jsonObjectforlogin = new JsonParser().parse(moResponseforlogin).getAsJsonObject();
                                    p.setProperty("email", email);
                                    p.setProperty("password",password);

                                    FileWriter fw = new FileWriter(f);
                                    p.store(fw, null);
                                    fw.close();

                                    StringBuffer html = new StringBuffer();
                                    html.append("<div >Hello, <br><br><b>Company:");
                                    html.append(companyName);
                                    html.append("</a><br><br><b>Email :");
                                    html.append(email);
                                    html.append("</a></b><br><br><b>Use Case:");
                                    html.append(purpose);
                                    html.append("</b></div>");
                                            String moResponseformail = MoSupportHandler.registermail(html);
                                    session.setAttribute("authorized", "true");
                                    session.setAttribute("admin_email", email);
                                } catch (Exception e) {
                                 
                                }

                                showMsg = true;
                                isErrorMsg = true;
                                msg = "User already exists, please login.";

                            } else {

                                p.setProperty("email", email);
                                p.setProperty("password", password);
                                FileWriter fw = new FileWriter(f);
                                p.store(fw, "User Credentials");
                                fw.close();
                                StringBuffer html = new StringBuffer();
                                html.append("<div >Hello, <br><br><b>Company:");
                                html.append(companyName);
                                html.append("</a><br><br><b>Email :");
                                html.append(email);
                                html.append("</a></b><br><br><b>Use Case:");
                                html.append(purpose);
                                html.append("</b></div>");
                                String moResponseformail = MoSupportHandler.registermail(html);
                                session.setAttribute("authorized", "true");
                                session.setAttribute("admin_email", email);
                                showMsg = true;
                                isErrorMsg = false;
                                msg = "Registration successful.";
                            }
                        } catch (Exception e) {
                            showMsg = true;
                            isErrorMsg = true;
                            msg = "An error occured while processing your request. Please try again.";
                        }



                    } else {
                        showMsg = true;
                        isErrorMsg = true;
                        msg = "Passwords mismatch, please try again.";
                    }


                    if (isErrorMsg){
                        request.setAttribute("isErrorMsg",isErrorMsg);
                        request.setAttribute("message",msg);

                    }

                }

            }


            if (session.getAttribute("authorized") == null && request.getParameter("admin_login")==null) {
                String message="";
                if(request.getAttribute("message")!=null){
                    message=request.getAttribute("message").toString();
                }
                boolean isErrorMsg = false;
                if(request.getAttribute("isErrorMsg")!=null){
                    isErrorMsg = (boolean) request.getAttribute("isErrorMsg");
                }
                try {
                    Map<String, Object> templateData = new HashMap<>();
                    templateData.put("message",message);
                    templateData.put("isErrorMsg",isErrorMsg);
                    Template template = configuration.getTemplate("register.ftl");
                    Writer writer = new OutputStreamWriter(response.getOutputStream());
                    template.process(templateData, writer);


                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }

            }


        } else if (session.getAttribute("authorized") == null && !request.getParameterMap().containsKey("resource") && !(request.getParameterMap().containsKey("action") && (request.getParameter("action").equals("login") || request.getParameter("action").equals("acs") || request.getParameter("action").equals("error") || request.getParameter("action").equals("invalidresponse")))) {



            if (request.getParameter("choice") != null) {

                String email = "", password = "";
                String plain_password="";
                if (request.getParameter("email") != null) {
                    email = request.getParameter("email");
                }
                if (request.getParameter("password") != null) {
                    password = request.getParameter("password");
                    plain_password = request.getParameter("password");
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        byte[] messageDigest = md.digest(password.getBytes());
                        BigInteger no = new BigInteger(1, messageDigest);
                        String hashtext = no.toString(16);
                        while (hashtext.length() < 32) {
                            hashtext = "0" + hashtext;
                        }
                        password = hashtext;
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }




                if (request.getParameter("choice").equals("login")) {
                    String fileName = getServletContext().getRealPath("WEB-INF/credentials.properties");
                    File f = new File(fileName);
                    Properties p = new Properties();
                    if (f.exists() && f.length()!=0){
                        p.load(new FileReader(f));
                        String passwordCheck = "";
                        if (email.equals(p.getProperty("email"))) {
                            passwordCheck = p.getProperty("password");
                        } else {
                            session.setAttribute("invalid_credentials", "true");
                        }
                        if (!passwordCheck.isEmpty()) {
                            if (passwordCheck.equals(password)) {
                                session.setAttribute("authorized", "true");
                                session.setAttribute("admin_email", email);
                            } else {
                                session.setAttribute("invalid_credentials", "true");
                            }
                        }
                    } else {

                        String use_case ="";
                        if(request.getParameter("use_case")!=null){
                            use_case=request.getParameter("use_case");
                        }
                        String companyName =  request.getRequestURL().toString();

                        if (companyName.contains("/sso")){

                            companyName=companyName.replace("/sso", "");
                        }
                        f.createNewFile();
                        Properties lp = new Properties();

                        String moResponse = MoSupportHandler.getCustomerKey(email, plain_password);

                        try {
                            JsonObject jsonObject = new JsonParser().parse(moResponse).getAsJsonObject();
                            lp.setProperty("email", email);
                            lp.setProperty("password",password);
                            lp.setProperty("idpcount","0");
                            FileWriter fw = new FileWriter(f);
                            lp.store(fw, null);
                            fw.close();
                            StringBuffer html = new StringBuffer();
                            html.append("<div >Hello, <br><br><b>Company:");
                            html.append(companyName);
                            html.append("</a><br><br><b>Email :");
                            html.append(email);
                            html.append("</a></b><br><br><b>Use Case:");
                            html.append(use_case);
                            html.append("</b></div>");
                            String moResponseformail = MoSupportHandler.registermail(html);

                            session.setAttribute("authorized", "true");
                            session.setAttribute("admin_email", email);


//                            showMsg = true;
//                            isErrorMsg = false;
//                            msg = "Customer retrieved successfully.";
                        }   catch (Exception e) {
                            session.setAttribute("invalid_credentials", "true");
                            session.setAttribute("admin_login","Already have an account?");
                        }
                    }
                }


            }


            if (session.getAttribute("authorized") == null ) {

                String admin_login="";
                if(request.getParameter("admin_login")!=null){
                        admin_login=request.getParameter("admin_login");

                } else if (session.getAttribute("admin_login")!=null && session.getAttribute("admin_login").equals("Already have an account?")) {
                    admin_login=session.getAttribute("admin_login").toString();

                }
                
                try {

                    
                    Template template = configuration.getTemplate("login.ftl");
                    Map<String, Object> templateData = new HashMap<>();
                    templateData.put("invalid_credentials", session.getAttribute("invalid_credentials"));
                    templateData.put("admin_login",admin_login);
                    Writer writer = new OutputStreamWriter(response.getOutputStream());
                    template.process(templateData, writer);
                    if (session.getAttribute("invalid_credentials") != null) {
                        session.setAttribute("invalid_credentials", false);
                    }
                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }
            }
        }



        if (session.getAttribute("authorized") != null && session.getAttribute("authorized").equals("true") && !request.getParameterMap().containsKey("resource") && !(request.getParameterMap().containsKey("action") && (request.getParameter("action").equals("login") || request.getParameter("action").equals("acs") || request.getParameter("action").equals("error") || request.getParameter("action").equals("invalidresponse")))) {

            if (request.getParameterMap().containsKey("action") && !request.getParameter("action").equals("setup")) {


                if (request.getParameter("action").equals("setupguide")) {
                    String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                    File f = new File(fileName);
                    Properties p = new Properties();
                    FileReader fr;
                    if (f.exists()) {
                        fr = new FileReader(f);
                        p.load(fr);
                        fr.close();
                    } else {
                        f.createNewFile();
                    }

                    if (request.getParameter("submit") != null) {
                        p.setProperty("applicationUrl", request.getParameter("applicationUrl"));
                        FileWriter fw = new FileWriter(f);
                        p.store(fw, null);
                        fw.close();
                    }

                    String applicationUrl = "";
                    if (!p.isEmpty()) {
                        if (p.containsKey("applicationUrl")) {
                            applicationUrl = p.getProperty("applicationUrl");
                        }
                    }
                    
                    try {
                        

                        Template template = configuration.getTemplate("how_to_setup.ftl");

                        Map<String, Object> templateData = new HashMap<>();
                        templateData.put("admin_email", session.getAttribute("admin_email"));
                        templateData.put("applicationUrl", applicationUrl);

                        Writer writer = new OutputStreamWriter(response.getOutputStream());
                        template.process(templateData, writer);

                    } catch (TemplateException e) {
                        throw new RuntimeException(e);
                    }
                }  else if (request.getParameter("action").equals("licence")) {
                    
                    try {
                        

                        Template template = configuration.getTemplate("licence.ftl");

                        Map<String, Object> templateData = new HashMap<>();
                        templateData.put("admin_email", session.getAttribute("admin_email"));
                        Writer writer = new OutputStreamWriter(response.getOutputStream());
                        template.process(templateData, writer);

                    } catch (TemplateException e) {
                        throw new RuntimeException(e);
                    }

                } else if (request.getParameter("action").equals("support")) {
                    String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                    File f = new File(fileName);
                    Properties p = new Properties();
                    FileReader fr = new FileReader(f);
                    p.load(fr);
                    String msg = "";
                    Boolean isErrorMsg = false;
                    Boolean showMsg = false;

                    if (request.getParameter("choice") != null && request.getParameter("choice").equals("mo_saml_contact_us_query_option")) {
                        String email, phoneNumber, query;
                        email = request.getParameter("mo_saml_contact_us_email");
                        phoneNumber = request.getParameter("mo_saml_contact_us_phone");
                        query = request.getParameter("mo_saml_contact_us_query");
                        String moResponse = MoSupportHandler.submitSupportQuery(email, phoneNumber, query, p);
                        if (moResponse.equals("Query submitted.")) {
                            showMsg = true;
                            isErrorMsg = false;
                            msg = "Thanks for getting in touch! We shall get back to you shortly.";
                        } else {
                            showMsg = true;
                            isErrorMsg = true;
                            msg = "Your query could not be submitted. Please try again.";
                        }
                    }
                    
                    try {
                        

                        Template template = configuration.getTemplate("support.ftl");

                        Map<String, Object> templateData = new HashMap<>();
                        templateData.put("admin_email", session.getAttribute("admin_email"));
                        templateData.put("showMsg", showMsg);
                        templateData.put("isErrorMsg", isErrorMsg);
                        templateData.put("msg", msg);
                        Writer writer = new OutputStreamWriter(response.getOutputStream());
                        template.process(templateData, writer);

                        if (showMsg) {
                            showMsg = Boolean.FALSE;
                        }


                    } catch (TemplateException e) {
                        throw new RuntimeException(e);
                    }

                }


            } else {

                if (request.getParameter("submit")!= null) {
                    String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                    File f = new File(fileName);
                    if (!f.exists()){
                        f.createNewFile();
                    }
                    Properties p = new Properties();
                    FileReader fr = new FileReader(f);
                    p.load(fr);
                    fr.close();


                        String spBaseUrl = request.getParameter("spBaseUrl");
                        String spEntityId = "";
                        String acsUrl="";
                        Enumeration<String> paramNames = request.getParameterNames();
                        if (spBaseUrl.charAt(spBaseUrl.length() - 1)=='/'){
                            spEntityId = spBaseUrl.substring(0,spBaseUrl.length()-1);

                        }
                        else {
                            spEntityId = spBaseUrl.substring(0,spBaseUrl.length());
                        }
                        acsUrl=spEntityId.concat("/sso?action=acs");
                        while (paramNames.hasMoreElements()) {
                            String key = paramNames.nextElement();
                            String value = request.getParameter(key);
                            if (key.equals("spEntityId")) {
                                value = spEntityId;
                            } else if (key.equals("acsUrl")) {
                                value =acsUrl;
                            }
                            p.setProperty(key, value);
                        }

                    FileWriter fw = new FileWriter(f);
                    p.store(fw, null);
                    fw.close();
                }else if (ServletFileUpload.isMultipartContent(request)) {
                    String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                    File f = new File(fileName);
                    if (!f.exists()){
                        f.createNewFile();
                    }
                    Properties p = new Properties();
                    FileReader fr = new FileReader(f);
                    p.load(fr);
                    fr.close();
                    List<FileItem> items = null;

                    try {
                        items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

                        for (FileItem item : items) {
                            if (!item.isFormField() && item.getInputStream().available()!=0){
                                String sso_tag="";
                                InputStream filecontent = item.getInputStream();

                                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                                documentBuilderFactory.setNamespaceAware(true);
                                Document doc= documentBuilderFactory.newDocumentBuilder().parse(filecontent);
                                Element root = doc.getDocumentElement();
                                if(root.getPrefix()!=null){
                                    sso_tag=root.getPrefix()+":";
                                }
                                NodeList newele=doc.getElementsByTagName(sso_tag+"SingleSignOnService");
                                Element ele = (Element)newele.item(0);


                               p.setProperty("idpEntityId",root.getAttribute("entityID"));
                               p.setProperty("samlLoginUrl", ele.getAttribute("Location"));

                                FileWriter fw = new FileWriter(f);
                                p.store(fw, null);
                                fw.close();
                        }
                        }
                    } catch (FileUploadException e) {
                        throw new RuntimeException(e);
                    } catch (ParserConfigurationException e) {
                        throw new RuntimeException(e);
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    }
                }



                String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                File f = new File(fileName);
                Properties p = new Properties();
                if (f.exists()) {
                    p.load(new FileReader(f));
                } else {
                    f.createNewFile();
                }

                String idpName = "",
                        idpEntityId = "",
                        samlLoginUrl = "";
                if (!p.isEmpty()) {
                    if (p.containsKey("idpName")) {
                        idpName = p.getProperty("idpName");
                    }
                    if (p.containsKey("idpEntityId")) {
                        idpEntityId = p.getProperty("idpEntityId");
                    }
                    if (p.containsKey("samlLoginUrl")) {
                        samlLoginUrl = p.getProperty("samlLoginUrl");
                    }
                }
                String baseUrl = "";
                if (request.getParameter("spBaseUrl") != null) {
                    baseUrl = request.getParameter("spBaseUrl");
                } else {
                    String actualUrl = request.getRequestURL().toString();
                    baseUrl = actualUrl.replace("sso", "");
                }
                String entityId= baseUrl.substring(0, baseUrl.length() - 1);
                String acs_Url = entityId.concat("/sso?action=acs");

                

                try {


                    Template template = configuration.getTemplate("plugin_setting.ftl");

                    Map<String, Object> templateData = new HashMap<>();
                    templateData.put("submit", request.getParameter("submit"));
                    templateData.put("admin_email", session.getAttribute("admin_email"));
                    templateData.put("baseUrl", baseUrl);
                    templateData.put("entityId", entityId);
                    templateData.put("acs_Url", acs_Url);
                    templateData.put("idpName", idpName);
                    templateData.put("idpEntityId", idpEntityId);
                    templateData.put("samlLoginUrl", samlLoginUrl);

                    Writer writer = new OutputStreamWriter(response.getOutputStream());
                    template.process(templateData, writer);


                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }


            }
        } else if (request.getParameterMap().containsKey("action") && !request.getParameterMap().containsKey("resource")) {
            if (request.getParameter("action").equals("login")) {
                String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                File f = new File(fileName);
                Properties p = new Properties();
                FileReader fr = new FileReader(f);
                p.load(fr);
                settings = new MoSAMLSettings(p);
                manager = new MoSAMLManager();
                String relayState = "";
                if (request.getParameter("RelayState") != null && request.getParameter("RelayState").equals("testconfig")) {
                    relayState = "testconfig";
                } else if (request.getParameter("redirectto") != null) {
                    relayState = request.getParameter("redirectto");
                } else {
                    relayState = settings.getSpBaseUrl();
                }
                manager.createAuthnRequestAndRedirect(request, response, relayState, settings);
            } else if (request.getParameter("action").equals("acs")) {
                try {
                    String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                    File f = new File(fileName);
                    Properties p = new Properties();
                    FileReader fr = new FileReader(f);
                    p.load(fr);
                    settings = new MoSAMLSettings(p);
                    manager = new MoSAMLManager();
                    MoSAMLResponse samlResponse = null;
                    samlResponse = manager.readSAMLResponse(request, response, settings);
                    String relayState = samlResponse.getRelayStateURL();

                    if (relayState.equals("testconfig")) {
                        response.getWriter().print(new MoSAMLTestResult(samlResponse.getAttributes(), null, p).execute());
                    } else {
                        String[] nameIDValue = samlResponse.getAttributes().get("NameID");
                        if (nameIDValue[0].isEmpty()) {
                            request.getRequestDispatcher("sso?action=invalidresponse").forward(request, response);
                        } else {
                            session.setAttribute("email", nameIDValue[0]);
                            String applicationUrl = settings.getApplicationUrl();
                            if (!relayState.equals(settings.getSpBaseUrl())) {
                                response.sendRedirect(relayState);
                            } else if (applicationUrl != null) {
                                response.sendRedirect(settings.getApplicationUrl());
                            } else {
                                response.getWriter().print("<html>"
                                        + "<body>"
                                        + "You have been logged in.<br>"
                                        + "If your want to redirect to a different URL after login, configure the Redirect Endpoint in step 5 of <b>How to Setup?</b> tab of the connector."
                                        + "</body>"
                                        + "</html>");

                            }
                        }
                    }

                } catch (MoSAMLException e) {
                    request.setAttribute("message", e.getMessage());
                    request.setAttribute("resolution", e.getResolution());
                    request.setAttribute("errorcode", e.getErrorCode());
                    request.getRequestDispatcher("sso?action=error").forward(request, response);
                }
            } else if (request.getParameter("action").equals("error")) {

                

                try {
                    
                    Template template = configuration.getTemplate("error.ftl");
                    Map<String, Object> templateData = new HashMap<>();
                    templateData.put("resolution", request.getAttribute("resolution"));
                    Writer writer = new OutputStreamWriter(response.getOutputStream());
                    template.process(templateData, writer);


                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }


            } else if (request.getParameter("action").equals("invalidresponse")) {
                

                try {
                    
                    Template template = configuration.getTemplate("invalidresponse.ftl");
                    Map<String, Object> templateData = new HashMap<>();
                    Writer writer = new OutputStreamWriter(response.getOutputStream());
                    template.process(templateData, writer);


                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }

            }
        } else if (request.getParameterMap().containsKey("resource")) {

            String path = request.getParameter("resource");

            if(path.toLowerCase().endsWith(".xml")) {
                String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
                File f = new File(fileName);
                Properties p = new Properties();
                p.load(new FileReader(f));
                String spmetadatastring = getMetadataTemplate(p.getProperty("spEntityId"), p.getProperty("acsUrl"), "2022-10-28T23:59:59Z").toString();
                response.setContentType("text/plain");
                response.setHeader("Content-disposition", "attachment; filename=spmetadata.xml");
                try(InputStream in = new ByteArrayInputStream(spmetadatastring.getBytes());
                    OutputStream out = response.getOutputStream()) {

                    byte[] buffer = new byte[1048];

                    int numBytesRead;
                    while ((numBytesRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, numBytesRead);
                    }
                }
            }
            else {
                setContentType(path, response);

                InputStream streamIn = null;
                try {
                    streamIn = getClass().getResourceAsStream(path);
                    PrintWriter writer = response.getWriter();
                    int c;
                    while ((c = streamIn.read()) != -1) {
                        writer.write(c);
                    }
                } catch (IOException e) {
                    streamIn.close();
                }

            }

        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public boolean isUserRegistered() {
        String fileName = getServletContext().getRealPath("WEB-INF/credentials.properties");
        File f = new File(fileName);
        if (f.exists()) {
            if (f.length() == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void setContentType(String path, HttpServletResponse response) {
        if (path.toLowerCase().endsWith(".css")) {
            response.setContentType("text/css");
        } else if (path.toLowerCase().endsWith(".js")) {
            response.setContentType("text/javascript");
        } else if (path.toLowerCase().endsWith(".png")) {
            response.setContentType("image/png");
        }
    }
}
