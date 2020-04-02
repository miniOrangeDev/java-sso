package com.miniorange.app.servlets;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miniorange.app.classes.MoSAMLException;
import com.miniorange.app.classes.MoSAMLResponse;
import com.miniorange.app.classes.MoSAMLTestResult;
import com.miniorange.app.helpers.MoSAMLManager;
import com.miniorange.app.helpers.MoSAMLSettings;
import com.miniorange.app.helpers.MoSupportHandler;


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
		if(request.getParameterMap().containsKey("action") && request.getParameter("action").equals("logout")) {
			if(session.getAttribute("authorized")!=null) {
				session.setAttribute("authorized",null);
			}
		}
		
		if(!isUserRegistered() && !request.getParameterMap().containsKey("resource")) {
			if(request.getParameter("choice")!=null) {
				String email="",password="";
				if(request.getParameter("email")!=null) {
					email = request.getParameter("email");
				}
				if(request.getParameter("password")!=null) {
					password = request.getParameter("password");
					try { 
			            MessageDigest md = MessageDigest.getInstance("SHA-1");  
			            byte[] messageDigest = md.digest(password.getBytes()); 
			            BigInteger no = new BigInteger(1, messageDigest); 
			            String hashtext = no.toString(16); 
			            while (hashtext.length() < 32) { 
			                hashtext = "0" + hashtext; 
			            }
			            password = hashtext;
			        } 
			        catch (NoSuchAlgorithmException e) { 
			            throw new RuntimeException(e); 
			        } 
				}
				if(request.getParameter("choice").equals("register")) {
					String fileName = getServletContext().getRealPath("WEB-INF/credentials.properties");
					File f = new File(fileName);
					if(!f.exists()) {
						f.createNewFile();		
					}
					Properties p = new Properties();
					p.setProperty("email",email);
					p.setProperty("password",password);
					p.store(new FileWriter(f),"User Credentials");
			    	
			    	session.setAttribute("authorized", "true");
					session.setAttribute("admin_email",email);
				}
				
			}	
			
			if(session.getAttribute("authorized") == null) {
				StringBuffer html = new StringBuffer();
				html.append("<!DOCTYPE html>\r\n" + 
						"<html>\r\n" + 
						"  <head>\r\n" + 
						"    <meta charset=\"utf-8\">\r\n" + 
						"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
						"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + 
						"    <!-- Main CSS-->\r\n" + 
						"    <link rel=\"stylesheet\" type=\"text/css\" href=\"?resource=/includes/css/main.css\">\r\n" + 
						"    <!-- Font-icon css-->\r\n" + 
						"    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n" + 
						"    <title>Register - miniOrange Admin</title>\r\n" + 
						"  </head>");
				
				html.append("<body>\r\n" + 
						"    <section class=\"material-half-bg\">\r\n" + 
						"      <div class=\"cover\"></div>\r\n" + 
						"    </section>\r\n" + 
						"    <section class=\"login-content\">\r\n" + 
						"      <div class=\"logo\">\r\n" + 
						"        <h1><img src=\"?resource=/resources/images/logo_large.png\"></h1>\r\n" + 
						"      </div>\r\n" + 
						"      <div class=\"col-md-6\">\r\n" + 
						"          <div class=\"tile\">\r\n" + 
						"            <h3 class=\"tile-title\">Register</h3>\r\n" + 
						"            <form class=\"register_form\" id=\"register_form\" method=\"POST\" action=\"\">\r\n" + 
						"            <input type=\"hidden\" name=\"choice\" value=\"register\">\r\n" + 
						"            <div class=\"tile-body\">\r\n" + 
						"              \r\n" + 
						"                <div class=\"form-group row\">\r\n" + 
						"                  <label class=\"control-label col-md-3\">Email</label>\r\n" + 
						"                  <div class=\"col-md-8\">\r\n" + 
						"                    <input class=\"form-control col-md-8\" type=\"email\" name=\"email\" placeholder=\"Enter email address\" required>\r\n" + 
						"                  </div>\r\n" + 
						"                </div>\r\n" + 
						"                <div class=\"form-group row\">\r\n" + 
						"                <label class=\"control-label col-md-3\">Password</label>\r\n" + 
						"                <div class=\"col-md-8\">\r\n" + 
						"                  <input class=\"form-control col-md-8\" type=\"password\" id=\"password\" name=\"password\" placeholder=\"Enter a password\" minlength=\"6\" required>\r\n" + 
						"                </div>\r\n" + 
						"                </div>\r\n" + 
						"                <div class=\"form-group row\">\r\n" + 
						"                <label class=\"control-label col-md-3\">Confirm Password</label>\r\n" + 
						"                <div class=\"col-md-8\">\r\n" + 
						"                  <input class=\"form-control col-md-8\" type=\"password\" id=\"confirm_password\" placeholder=\"Re-type the password\" minlength=\"6\" required>\r\n" + 
						"                </div>\r\n" + 
						"                </div>\r\n" + 
						"                <script>\r\n" + 
						"                var password = document.getElementById(\"password\")\r\n" + 
						"                , confirm_password = document.getElementById(\"confirm_password\");\r\n" + 
						"\r\n" + 
						"                function validatePassword(){\r\n" + 
						"                  if(password.value != confirm_password.value) {\r\n" + 
						"                    confirm_password.setCustomValidity(\"Passwords Don't Match\");\r\n" + 
						"                  } else {\r\n" + 
						"                    confirm_password.setCustomValidity('');\r\n" + 
						"                  }\r\n" + 
						"                }\r\n" + 
						"\r\n" + 
						"                password.onchange = validatePassword;\r\n" + 
						"                confirm_password.onkeyup = validatePassword;\r\n" + 
						"                </script>\r\n" + 
						"                \r\n" + 
						"            </div>\r\n" + 
						"            <div class=\"tile-footer\">\r\n" + 
						"              <div class=\"row\">\r\n" + 
						"                <div class=\"col-md-8 col-md-offset-3\">\r\n" + 
						"                  <button class=\"btn btn-primary\" type=\"submit\" id=\"register\"><i class=\"fa fa-fw fa-lg fa-check-circle\"></i>Register</button>\r\n" + 
						"                </div>\r\n" + 
						"              </div>\r\n" + 
						"            </div>\r\n" + 
						"            </form>\r\n" + 
						"          </div>\r\n" + 
						"        </div>\r\n" + 
						"\r\n" + 
						"    </section>\r\n" + 
						"    <!-- Essential javascripts for application to work-->\r\n" + 
						"    <script src=\"?resource=/includes/js/jquery-3.2.1.min.js\"></script>\r\n" + 
						"    <script src=\"?resource=/includes/js/popper.min.js\"></script>\r\n" + 
						"    <script src=\"?resource=/includes/js/bootstrap.min.js\"></script>\r\n" + 
						"    <script src=\"?resource=/includes/js/main.js\"></script>\r\n" + 
						"    <!-- The javascript plugin to display page loading on top-->\r\n" + 
						"    <script src=\"?resource=/includes/js/plugins/pace.min.js\"></script>\r\n" + 
						"  </body>\r\n" + 
						"</html>");
				response.setCharacterEncoding("iso-8859-1");
				response.setContentType("text/html");
				response.getOutputStream().write(html.toString().getBytes("UTF-8"));
			}
			
		} 
		else if(session.getAttribute("authorized")==null && !request.getParameterMap().containsKey("resource") && !(request.getParameterMap().containsKey("action") && (request.getParameter("action").equals("login") || request.getParameter("action").equals("acs") || request.getParameter("action").equals("error") || request.getParameter("action").equals("invalidresponse")))){
			if(request.getParameter("choice")!=null) {
				String email="",password="";
				if(request.getParameter("email")!=null) {
					email = request.getParameter("email");
				}
				if(request.getParameter("password")!=null) {
					password = request.getParameter("password");
					try { 
			            MessageDigest md = MessageDigest.getInstance("SHA-1");  
			            byte[] messageDigest = md.digest(password.getBytes()); 
			            BigInteger no = new BigInteger(1, messageDigest); 
			            String hashtext = no.toString(16); 
			            while (hashtext.length() < 32) { 
			                hashtext = "0" + hashtext; 
			            }
			            password = hashtext;
			        } 
			        catch (NoSuchAlgorithmException e) { 
			            throw new RuntimeException(e); 
			        } 
				}
				if(request.getParameter("choice").equals("login")) {
					String fileName = getServletContext().getRealPath("WEB-INF/credentials.properties");
					File f = new File(fileName);
					Properties p = new Properties();
					if(f.exists()) {
						p.load(new FileReader(f));	
					}
					String passwordCheck="";
					if(email.equals(p.getProperty("email"))) {
						passwordCheck = p.getProperty("password");
					}
					else {
						session.setAttribute("invalid_credentials","true");
					}
					
					if(!passwordCheck.isEmpty()) {
						if(passwordCheck.equals(password)) {
							session.setAttribute("authorized", "true");
							session.setAttribute("admin_email",email);
						}
						else {
							session.setAttribute("invalid_credentials","true");
						}
					}
				}
			}
			
			if(session.getAttribute("authorized") == null) {
				StringBuffer html = new StringBuffer();
				html.append("<!DOCTYPE html>\r\n" + 
						"<html>\r\n" + 
						"  <head>\r\n" + 
						"    <meta charset=\"utf-8\">\r\n" + 
						"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
						"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + 
						"    <!-- Main CSS-->\r\n" + 
						"    <link rel=\"stylesheet\" type=\"text/css\" href=\"?resource=/includes/css/main.css\">\r\n" + 
						"    <!-- Font-icon css-->\r\n" + 
						"    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n" + 
						"    <title>Login - miniOrange Admin</title>\r\n" + 
						"  </head>\r\n" + 
						"  <body>\r\n" + 
						"  <section class=\"material-half-bg\">\r\n" + 
						"      <div class=\"cover\"></div>\r\n" + 
						"    </section>\r\n" + 
						"    <section class=\"login-content\">\r\n" + 
						"      <div class=\"logo\">\r\n" + 
						"        <h1><img src=\"?resource=/resources/images/logo_large.png\"></h1>\r\n" + 
						"      </div>\r\n" + 
						"      <div class=\"col-md-6\">\r\n" + 
						"          <div class=\"tile\">\r\n" + 
						"            <h3 class=\"tile-title\">Login with miniOrange</h3>\r\n" + 
						"            <form class=\"login_form\" method=\"POST\" action=\"?action=setup\">\r\n" + 
						"            <input type=\"hidden\" name=\"choice\" value=\"login\">\r\n" + 
						"            <div class=\"tile-body\">\r\n" + 
						"              <div class=\"form-group row\">\r\n" + 
						"                  <label class=\"control-label col-md-3\">Email</label>\r\n" + 
						"                  <div class=\"col-md-8\">\r\n" + 
						"                    <input class=\"form-control col-md-8\" type=\"email\" name=\"email\" placeholder=\"Enter email address\" required>\r\n" + 
						"                  </div>\r\n" + 
						"                </div>\r\n" + 
						"                <div class=\"form-group row\">\r\n" + 
						"                <label class=\"control-label col-md-3\">Password</label>\r\n" + 
						"                <div class=\"col-md-8\">\r\n" + 
						"                  <input class=\"form-control col-md-8\" type=\"password\" name=\"password\" id=\"password\" placeholder=\"Enter a password\" minlength=\"6\" required>\r\n" + 
						"                </div>\r\n" + 
						"                </div>\r\n" + 
						"            </div>\r\n" + 
						"            <div class=\"tile-footer\">\r\n" + 
						"              <div class=\"row\">\r\n" + 
						"                <div class=\"col-md-8 col-md-offset-3\">\r\n" + 
						"                <button class=\"btn btn-primary\" type=\"submit\"><i class=\"fa fa-fw fa-lg fa-check-circle\"></i>Login</button>\r\n" + 
						"                </div>\r\n" + 
						"              </div>\r\n" + 
						"            </div>\r\n" + 
						"            </form>\r\n" + 
						"          </div>\r\n" + 
						"        </div>\r\n" + 
						"    </section>\r\n" + 
						"      \r\n" + 
						"\r\n" + 
						"    <!-- Essential javascripts for application to work-->\r\n" + 
						"    <script src=\"?resource=/includes/js/jquery-3.2.1.min.js\"></script>\r\n" + 
						"    <script src=\"?resource=/includes/js/popper.min.js\"></script>\r\n" + 
						"    <script src=\"?resource=/includes/js/bootstrap.min.js\"></script>\r\n" + 
						"    <script src=\"?resource=/includes/js/main.js\"></script>\r\n" + 
						"    <!-- The javascript plugin to display page loading on top-->\r\n" + 
						"    <script src=\"?resource=/includes/js/plugins/pace.min.js\"></script>\r\n" + 
						"    <script type=\"text/javascript\" src=\"?resource=/includes/js/plugins/bootstrap-notify.min.js\"></script>\r\n" + 
						"    <script type=\"text/javascript\" src=\"?resource=/includes/js/plugins/sweetalert.min.js\"></script>\r\n"); 
				
				if(session.getAttribute("invalid_credentials")!=null) {
			    	if(session.getAttribute("invalid_credentials").equals("true")) {
			    		html.append("<script>"
			    				+ "$(document).ready(function(){"
			    				+ "$.notify({"
			    				+ "title: \"ERROR : \","
			    				+ "message: \"Invalid username or password\","
			    				+ "icon: 'fa fa-times'"
			    				+ "},{"
			    				+ "type: \"danger\""
			    				+ "});"
			    				+ "});"
			    				+ "</script>\");");        
			    	}
			    	session.setAttribute("invalid_credentials","false");
			    }		
				html.append("  </body>\r\n" + 
							"</html>");
				response.setCharacterEncoding("iso-8859-1");
				response.setContentType("text/html");
				response.getOutputStream().write(html.toString().getBytes("UTF-8"));
			}
		}
		if(session.getAttribute("authorized")!=null && session.getAttribute("authorized").equals("true") && !request.getParameterMap().containsKey("resource") && !(request.getParameterMap().containsKey("action") && (request.getParameter("action").equals("login") || request.getParameter("action").equals("acs") || request.getParameter("action").equals("error") || request.getParameter("action").equals("invalidresponse")))) {
			String htmlStart = "<!DOCTYPE html>\r\n" + 
					"<html lang=\"en\">\r\n" + 
					"    <head>\r\n" + 
					"        <meta charset=\"utf-8\">\r\n" + 
					"        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
					"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + 
					"        <!-- Main CSS-->\r\n" + 
					"        <link rel=\"stylesheet\" type=\"text/css\" href=\"?resource=/includes/css/main.css\">\r\n" + 
					"        <!-- Font-icon css-->\r\n" + 
					"        <link rel=\"stylesheet\" type=\"text/css\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n" + 
					"    </head>\r\n" + 
					"    <body class=\"app sidebar-mini rtl\">\r\n" + 
					"        <!-- Navbar-->\r\n" + 
					"        <header class=\"app-header\"><a class=\"app-header__logo\" href=\"#\" style=\"margin-top:10px;\"><img src=\"?resource=/resources/images/logo-home.png\"></a>\r\n" + 
					"        <!-- Sidebar toggle button<a class=\"app-sidebar__toggle\" href=\"#\" data-toggle=\"sidebar\" aria-label=\"Hide Sidebar\"></a> -->\r\n" + 
					"        <ul class=\"app-nav\">\r\n" + 
					"          <li class=\"dropdown\"><a class=\"app-nav__item\" href=\"#\" data-toggle=\"dropdown\" aria-label=\"Open Profile Menu\"><i class=\"fa fa-user fa-lg\"><span  style=\"margin-left:5px\">"+session.getAttribute("admin_email")+"</span><span style=\"padding-left:5px;\"><i class=\"fa fa-caret-down\"></i></span></i></a>\r\n" + 
					"            <ul class=\"dropdown-menu settings-menu dropdown-menu-right\">\r\n" + 
					"          <li><a class=\"dropdown-item\" href=\"?action=logout\"><i class=\"fa fa-sign-out fa-lg\"></i> Logout</a></li>\r\n" + 
					"        </ul>\r\n" + 
					"        </li>\r\n" + 
					"    </ul>\r\n" + 
					"        </header>\r\n" + 
					"        <!-- Sidebar menu-->\r\n" + 
					"    <div class=\"app-sidebar__overlay\" data-toggle=\"sidebar\"></div>\r\n" + 
					"    \r\n" + 
					"    <aside class=\"app-sidebar\">\r\n" + 
					"      <div class=\"app-sidebar__user\" style=\"padding-left:40px\"><img src=\"?resource=/resources/images/miniorange.png\"  style=\"width:37.25px; height:50px;\" alt=\"User Image\">\r\n" + 
					"        <div style=\"margin-left:15px;\">\r\n" + 
					"          <p class=\"app-sidebar__user-name\">JAVA SAML</p>\r\n" + 
					"          <p class=\"app-sidebar__user-designation\">Connector</p>\r\n" + 
					"        </div>\r\n" + 
					"      </div>\r\n" + 
					"      <ul class=\"app-menu\">\r\n";
			String htmlEnd = "<script src=\"?resource=/includes/js/jquery-3.2.1.min.js\"></script>\r\n" + 
					"    <script src=\"?resource=/includes/js/popper.min.js\"></script>\r\n" + 
					"    <script src=\"?resource=/includes/js/bootstrap.min.js\"></script>\r\n" + 
					"    <script src=\"?resource=/includes/js/main.js\"></script>\r\n" + 
					"    <script src=\"?resource=/includes/js/plugins/pace.min.js\"></script>\r\n" + 
					"    </body>\r\n" + 
					"</html>";
			if(request.getParameterMap().containsKey("action") && !request.getParameter("action").equals("setup")) {
				if(request.getParameter("action").equals("setupguide")) {
					String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
					File f = new File(fileName);
					Properties p = new Properties();
					FileReader fr; 
					if(f.exists()) {
						fr = new FileReader(f);
						p.load(fr);	
						fr.close();
					}
					else {
						f.createNewFile();
					}

					if(request.getParameter("submit")!=null) {
						p.setProperty("applicationUrl",request.getParameter("applicationUrl"));
						FileWriter fw = new FileWriter(f);
						p.store(fw,null);
						fw.close();
					}

					String applicationUrl = "";
					if(!p.isEmpty()) {
						if(p.containsKey("applicationUrl")) {
							applicationUrl = p.getProperty("applicationUrl");
						}
					}
					StringBuffer html = new StringBuffer();
					html.append(htmlStart);
					html.append("<li><a class=\"app-menu__item\" href=\"?action=setup\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-gear\"></i><span class=\"app-menu__label\"><b>Plugin Settings</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item active\" href=\"?action=setupguide\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-info-circle\"></i><span class=\"app-menu__label\"><b>How to Setup?</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=licence\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-dollar\"></i><span class=\"app-menu__label\"><b>Licensing</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=support\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-support\"></i><span class=\"app-menu__label\"><b>Support</b></span></a></li>      </ul>\r\n" + 
							"    </aside>\r\n");
					html.append("<main class=\"app-content\">"
							+ "	<div class=\"row\">\r\n" + 
							"            <div class=\"col-md-12\">\r\n" + 
							"              <div class=\"tile\">\r\n" + 
							"                <div class=\"row\">\r\n" + 
							"                  <div class=\"col-lg-10\">\r\n" + 
							"                    <h3>Follow these steps to setup the plugin:</h3>\r\n" + 
							"                    <h4>Step 1:</h4>\r\n" + 
							"                    <ul>\r\n" + 
							"                      <li>You can configure the <b>SP Base URL</b> or leave this option as it is.</li>\r\n" + 
							"                      <li>You need to provide these <b>SP Entity ID</b> and <b>ACS URL</b> values while configuring your Identity Provider</li>\r\n" +  
							"                    </ul>\r\n" +
							"                    <img src=\"?resource=/resources/images/java_saml_connector_sp_settings_1.png\" style=\"width:800px;height:380px;margin-left:50px; border:1px solid;\"> \r\n" + 
							"                    <br/><br/>\r\n" + 
							"                    <h4>Step 2:</h4>\r\n" + 
							"                    <ul>\r\n" + 
							"                      <li>Use your Identity Provider details to configure the plugin.</li>\r\n" + 
							"                      <li>Click on the <b>Save</b> button to save your configuration.</li>\r\n" + 
							"                    </ul>\r\n" + 
							"                    <img src=\"?resource=/resources/images/java_saml_connector_idp_settings_1.png\" style=\"width:800px;height:380px;margin-left:50px; border:1px solid;\">\r\n" + 
							"                    <br/><br/>\r\n" + 
							"                    <h4>Step 3:</h4>\r\n" + 
							"                    <ul>\r\n" + 
							"                    <li>You can test if the plugin is configured properly or not by clicking  on the <b>Test Configuration</b> button.</li>\r\n" + 
							"                    </ul>\r\n" + 
							"                    <img src=\"?resource=/resources/images/java_saml_connector_test_configuration.png\" style=\"width:800px;height:380px;margin-left:50px; border:1px solid;\">\r\n" + 
							"                    <ul>\r\n" + 
							"                    <br/>\r\n" + 
							"                    <li>If the configuration is correct, you should see a Test Successful screen with the user's attribute values.</li>\r\n" + 
							"                    </ul>\r\n" + 
							"                    <img src=\"?resource=/resources/images/java_saml_connector_test_result.png\" style=\"width:600px;height:400px;margin-left:50px; border:1px solid;\">\r\n" + 
							"                    <br/><br/>\r\n" + 
							"                    <h4>Step 4:</h4>\r\n" + 
							"                    <p>Use the following URL as a link in your application from where you want to perform SSO:<br/>\r\n" + 
							"                    <code>http://&lt;your-domain&gt;/&lt;application-name&gt;/sso?action=login</code></p>\r\n" + 
							"                    <p>OR<br/>\r\n" + 
							"                    <p>You can use the following URL if you want to redirect user to a specific page after the SSO:<br/>\r\n" + 
							"                    <code>http://&lt;your-domain&gt;/&lt;application-name&gt;/sso?action=login&redirectto=&lt;redirect-url&gt;</code></p>\r\n" + 
							"                    <p>For example, you can use it as:<br/>\r\n" + 
							"                    <code>&lt;a href=\"http://&lt;your-domain&gt;/&lt;application-name&gt;/sso?action=login&redirectto=/&lt;application-name&gt;/dashboard\"&gt;Log in&lt;/a&gt;</code>\r\n" + 
							"                    <h4>Step 5:</h4>\r\n" + 
							"\r\n" + 
							"                    <form id=\"setup_form\" method=\"POST\" action=\"\">\r\n" + 
							"                      \r\n" + 
							"                      <div class=\"form-group\">\r\n" + 
							"                        <label><b>Redirect Endpoint</b><span class=\"badge badge-pill badge-primary float-right\" onclick=\"\" title=\"The users will be redirected to this URL after logging in.\"> ?</span>\r\n" + 
							"                        \r\n" + 
							"                        </label>\r\n" + 
							"                        <table style=\"width: 100%\">\r\n" + 
							"                        <tr>\r\n" + 
							"                        <td><input class=\"form-control\" name=\"applicationUrl\" id=\"applicationUrl\" type=\"text\"\r\n" + 
							"                        value=\""+applicationUrl+"\"></td>\r\n" + 
							"                        <td style=\"padding-left:20px\"><button class=\"btn btn-primary\" type=\"submit\" name=\"submit\" id=\"submit\">Submit</button></td>\r\n" + 
							"                        </tr>\r\n" + 
							"                        </table>\r\n" + 
							"                      </div>\r\n" + 
							"                     </form>\r\n" + 
							"                    <h4>Step 6:</h4>\r\n" + 
							"                      <p>Please provide your Redirect Endpoint in the above field. You can use the following code snippet in your application endpoint to retrieve the\r\n" + 
							"session attributes :</p>\r\n" + 
							"                      <figure style=\"background-color:#f2f2f2; border-radius:5px; border: #bfbfbf solid 1px\">\r\n" + 
							"                      <pre>\r\n" + 
							"                      <code>\r\n"
							+ "  HttpSession session = request.getSession(false);\r\n"
							+ "  String email = (String)session.getAttribute(\"email\");" + 
							"  </code></pre></figure>\r\n" + 
							"                      <p>The variable <code>email</code> will contain the received user attribute.</p>\r\n" + 
							"                  </div>\r\n" + 
							"                </div>\r\n" + 
							"              </div>\r\n" + 
							"            </div>\r\n" + 
							"        </div>\r\n" + 
							"    </main>");
					html.append(htmlEnd);
					response.setContentType("text/html; charset=ISO-8859-1");
					response.setCharacterEncoding("ISO-8859-1");
					response.getOutputStream().write(html.toString().getBytes("UTF-8"));
				}
				else if(request.getParameter("action").equals("licence")) {
					StringBuffer html = new StringBuffer();
					html.append("<!DOCTYPE html>\r\n" + 
							"<html lang=\"en\">\r\n" + 
							"    <head>\r\n" + 
							"        <meta charset=\"utf-8\">\r\n" + 
							"        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
							"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + 
							"        <!-- Main CSS-->\r\n" + 
							"        <link rel=\"stylesheet\" type=\"text/css\" href=\"?resource=/includes/css/main.css\">\r\n" + 
							"        <!-- Font-icon css-->\r\n" + 
							"        <link rel=\"stylesheet\" type=\"text/css\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n" + 
//							"        <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.6.3/css/all.css\">\r\n" + 
//							"		<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n" + 
							"     	<style>\r\n" + 
							"		    .contactus-link{\r\n" + 
							"		    font-weight:bold;\r\n" + 
							"		    }\r\n" + 
							"		    .medium-font-size{\r\n" + 
							"		    font-size:medium;\r\n" + 
							"		    }\r\n" + 
							"		    .upgrade-now:hover{\r\n" + 
							"		    color:#fff;\r\n" + 
							"		    font-weight:600;\r\n" + 
							"		    }\r\n" + 
							"		    .upgrade-now-link:hover{\r\n" + 
							"		    color:#fff!important;\r\n" + 
							"		    font-weight:600;\r\n" + 
							"		    }\r\n" + 
							"		    .select-design{\r\n" + 
							"		    border: 1px solid #ddd;\r\n" + 
							"		    background-color: #fff;\r\n" + 
							"		    color: #32373c;\r\n" + 
							"		    background-color: white;\r\n" + 
							"		    border-radius:4px;\r\n" + 
							"		    min-height:auto;\r\n" + 
							"		    }\r\n" + 
							"		    .upgrade-now-link:hover{\r\n" + 
							"		    opacity:0.5;\r\n" + 
							"		    cursor:pointer;\r\n" + 
							"		    }\r\n" + 
							"		    .center-pills { display: inline-block; }\r\n" + 
							"		    .nav-pills{\r\n" + 
							"		    border: 1px solid #2f6062;\r\n" + 
							"		    height:53px;\r\n" + 
							"		    border-radius: 50em;\r\n" + 
							"		    padding-top: 2px;\r\n" + 
							"		    padding-left:0.2em;\r\n" + 
							"		    padding-right:0.2em;\r\n" + 
							"		    }\r\n" + 
							"		    .nav-pills>li{\r\n" + 
							"		    width:200px;\r\n" + 
							"		    }\r\n" + 
							"		    .tab-font{\r\n" + 
							"		    vertical-align:text-bottom;\r\n" + 
							"		    font-size:15px;\r\n" + 
							"		    } \r\n" + 
							"		    .nav-pills>li+li {\r\n" + 
							"		    margin-left: 0px;\r\n" + 
							"		    }\r\n" + 
							"		    .nav-pills>li.active>a, .nav-pills>li.active>a:hover, .nav-pills>li.active>a:focus,.nav-pills>li.active>a:active{\r\n" + 
							"		    color: #fff;\r\n" + 
							"		    background-color:#3f8588;\r\n" + 
							"		    height:47px;\r\n" + 
							"		    border-radius: 50em;\r\n" + 
							"		    }\r\n" + 
							"		    .nav-pills>li>a:hover {\r\n" + 
							"		    color:#fff;\r\n" + 
							"		    background: #44cec1;\r\n" + 
							"		    height:46px;\r\n" + 
							"		    border-radius: 50em;\r\n" + 
							"		    }\r\n" + 
							"		    .nav-pills>li>a:focus{\r\n" + 
							"		    color:#fff;\r\n" + 
							"		    background:grey;\r\n" + 
							"		    height:47px;\r\n" + 
							"		    border-radius: 50em;\r\n" + 
							"		    }\r\n" + 
							"		    .nav-pills>li.active{\r\n" + 
							"		    background-color: #2f6062;\r\n" + 
							"		    border-radius: 50em;\r\n" + 
							"		    }\r\n" + 
							"		    .thumbnail{\r\n" + 
							"		    border: 1px solid #898080;\r\n" + 
							"		    border-radius: 4px;\r\n" + 
							"		    -webkit-transition: border .2s ease-in-out;\r\n" + 
							"		    -o-transition: border .2s ease-in-out;\r\n" + 
							"		    transition: border .2s ease-in-out;\r\n" + 
							"		    background: #8B7979 !important;\r\n" + 
							"		    width:300px;\r\n" + 
							"		    text-align:center;\r\n" + 
							"		    padding-right: 20px !important;\r\n" + 
							"		    }\r\n" + 
							"		    .img-size{\r\n" + 
							"		    max-width: 60%;\r\n" + 
							"		    margin-left: 20%;\r\n" + 
							"		    border: 1px solid black;\r\n" + 
							"		    }\r\n" + 
							"		    .mo-tab{\r\n" + 
							"		    padding:0px!important;\r\n" + 
							"		    margin-left:-150px !important;\r\n" + 
							"		    color:#fff !important;\r\n" + 
							"		    }\r\n" + 
							"		    .mo-tab&gt;p{\r\n" + 
							"		    text-align:center;\r\n" + 
							"		    font-size: 16px;\r\n" + 
							"		    }\r\n" + 
							"		    .btn-large{\r\n" + 
							"		    padding: 5px !important;\r\n" + 
							"		    font-size: 20px;\r\n" + 
							"		    }\r\n" + 
							"		    .mo-tab&gt;h3{\r\n" + 
							"		    margin-top:0% !important;\r\n" + 
							"		    }\r\n" + 
							"		    .nav-pills>li>a {\r\n" + 
							"		    border-radius: 0px;\r\n" + 
							"		    height:47px;\r\n" + 
							"		    border-color:#E85700;\r\n" + 
							"		    font-weight: 500;\r\n" + 
							"		    color: #101110;\r\n" + 
							"		    text-transform:uppercase;\r\n" + 
							"		    }\r\n" + 
							"		    .contactus-link a:hover{\r\n" + 
							"		    color:#00a0d2;\r\n" + 
							"		    }\r\n" + 
							"		    .contactus-link a{\r\n" + 
							"		    color:#0073aa;\r\n" + 
							"		    font-weight:600;\r\n" + 
							"		    }\r\n" + 
							"		    .upgrade-now-dropbtn {\r\n" + 
							"		    background-color: #000;\r\n" + 
							"		    color: white;\r\n" + 
							"		    padding: 16px;\r\n" + 
							"		    font-size: 16px;\r\n" + 
							"		    border: none;\r\n" + 
							"		    cursor: pointer;\r\n" + 
							"		    }\r\n" + 
							"		    .upgrade-now-dropdown {\r\n" + 
							"		    position: relative;\r\n" + 
							"		    display: inline-block;\r\n" + 
							"		    }\r\n" + 
							"		    .dropdown-content {\r\n" + 
							"		    display: none;\r\n" + 
							"		    position: absolute;\r\n" + 
							"		    background-color: #f9f9f9;\r\n" + 
							"		    min-width: 160px;\r\n" + 
							"		    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);\r\n" + 
							"		    z-index: 1;\r\n" + 
							"		    }\r\n" + 
							"		    .dropdown-menu-center {\r\n" + 
							"		    left: 50% !important;\r\n" + 
							"		    right: auto !important;\r\n" + 
							"		    text-align: center !important;\r\n" + 
							"		    transform: translate(-50%, 0) !important;\r\n" + 
							"		    width:100%;\r\n" + 
							"		    }\r\n" + 
							"		    .dropdown-content a {\r\n" + 
							"		    color: black;\r\n" + 
							"		    padding: 12px 16px;\r\n" + 
							"		    text-decoration: none;\r\n" + 
							"		    display: block;\r\n" + 
							"		    }\r\n" + 
							"		    .price li:nth-of-type(2n+1) {\r\n" + 
							"		    background-color: rgba(23, 61, 80, 0.06);\r\n" + 
							"		    }\r\n" + 
							"		    .price1 li:nth-of-type(2n) {\r\n" + 
							"		    background-color: rgba(23, 61, 80, 0.06);\r\n" + 
							"		    }\r\n" + 
							"		    .wp-sso .popover{\r\n" + 
							"		    background-color:#fff!important;\r\n" + 
							"		    color:#fff;\r\n" + 
							"		    font-size:1.5em;\r\n" + 
							"		    }\r\n" + 
							"		    .columns {\r\n" + 
							"		    float: left;\r\n" + 
							"		    width: 45.3%;\r\n" + 
							"		    padding: 9px;\r\n" + 
							"		    }\r\n" + 
							"		    .price {\r\n" + 
							"		    list-style-type: none;\r\n" + 
							"		    border: 2px solid #eee;\r\n" + 
							"		    margin: 0;\r\n" + 
							"		    padding: 0;\r\n" + 
							"		    -webkit-transition: 0.3s;\r\n" + 
							"		    transition: 0.3s;\r\n" + 
							"		    }\r\n" + 
							"		    .price:hover {\r\n" + 
							"		    box-shadow: 0 8px 12px 0 rgba(0,0,0,0.2)\r\n" + 
							"		    }\r\n" + 
							"		    .price li {\r\n" + 
							"		    border-bottom: 1px solid #eee;\r\n" + 
							"		    text-align: center;\r\n" + 
							"		    padding:15px;\r\n" + 
							"		    }\r\n" + 
							"		    .price1 {\r\n" + 
							"		    list-style-type: none;\r\n" + 
							"		    border: 2px solid #eee;\r\n" + 
							"		    margin: 0;\r\n" + 
							"		    padding: 0;\r\n" + 
							"		    -webkit-transition: 0.3s;\r\n" + 
							"		    transition: 0.3s;\r\n" + 
							"		    }\r\n" + 
							"		    .price1:hover {\r\n" + 
							"		    box-shadow: 0 8px 12px 0 rgba(0,0,0,0.2)\r\n" + 
							"		    }\r\n" + 
							"		    .price1 li {\r\n" + 
							"		    border-bottom: 1px solid #eee;\r\n" + 
							"		    text-align: center;\r\n" + 
							"		    padding:15px;\r\n" + 
							"		    }\r\n" + 
							"		    h4{\r\n" + 
							"		    font-size:15px;\r\n" + 
							"		    }\r\n" + 
							"		    .wp-sso .standard-enterprise h2{\r\n" + 
							"		      color: #2f6062;\r\n" + 
							"		        font-weight: 700;\r\n" + 
							"		        font-size: 1.7em;\r\n" + 
							"		        margin-bottom: 0em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .premium h2{\r\n" + 
							"		        color: #44cec1;\r\n" + 
							"		        font-weight: 700;\r\n" + 
							"		        font-size: 1.7em;\r\n" + 
							"		        margin-bottom: 0em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso h3{\r\n" + 
							"		       color: #000;\r\n" + 
							"		        font-weight: 500;\r\n" + 
							"		        font-size: 1.2em;\r\n" + 
							"		        padding-bottom:1em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .standard-enterprise .cd-value{\r\n" + 
							"		       color: #2f6062;\r\n" + 
							"		       font-weight: 200;\r\n" + 
							"		       font-size:2.5em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .premium .cd-value{\r\n" + 
							"		      color: #44cec1;\r\n" + 
							"		       font-weight: 200;\r\n" + 
							"		       font-size:2.5em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .cd-currency{\r\n" + 
							"		      font-weight: 500;\r\n" + 
							"		      color:#1a445866;\r\n" + 
							"		      font-size: 1.5em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso sup{\r\n" + 
							"		       top:-0.79em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso ul{\r\n" + 
							"		       list-style-type:none;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .header{\r\n" + 
							"		      background:#f2f5f8;\r\n" + 
							"		      margin:0px;\r\n" + 
							"		      list-style-type:none;\r\n" + 
							"		      padding:2em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .upgrade-now{\r\n" + 
							"		      display:block;\r\n" + 
							"		      margin:0px;\r\n" + 
							"		      text-transform:uppercase;\r\n" + 
							"		      padding: 0em;\r\n" + 
							"		      background: #000;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .upgrade-now-link{\r\n" + 
							"		        display: block;\r\n" + 
							"		        padding: 2em 1em 2em 1em;\r\n" + 
							"		        color: #fff;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso ul li{\r\n" + 
							"		      margin:0px;\r\n" + 
							"		      list-style:none;\r\n" + 
							"		      font-size: 0.9em;\r\n" + 
							"		      padding: 0.8em;\r\n" + 
							"		      font-weight: 500;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .bold-features{\r\n" + 
							"		        padding: 0.5em;\r\n" + 
							"		        font-size: 0.8em;\r\n" + 
							"		        color: #0bb3a3;\r\n" + 
							"		        font-weight:700;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso{\r\n" + 
							"		      margin-top:2em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso ul{\r\n" + 
							"		      padding:0em;\r\n" + 
							"		      margin-left:0px;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    .wp-sso .contactus-margin{\r\n" + 
							"		       margin-bottom:2.4em;\r\n" + 
							"		    }\r\n" + 
							"		\r\n" + 
							"		    #dollar{\r\n" + 
							"		      top:-1.79em;\r\n" + 
							"		    }\r\n" + 
							"		</style>\r\n" + 
							"    	<script>\r\n" + 
							"	    	function upgradeform(planType){\r\n" + 
							"	    	  	$('#requestOrigin').val(planType);\r\n" + 
							"	    	  	$('#loginform').submit();\r\n" + 
							"	    	  }\r\n" + 
							"	    </script>\r\n" + 
							"    </head>\r\n" + 
							"    <body class=\"app sidebar-mini rtl\">\r\n" + 
							"    	<form style=\"display:none;\" id=\"loginform\" action=\"https://login.xecurify.com/moas/login\" target=\"_blank\" method=\"post\">\r\n" + 
							"			<input type=\"text\" name=\"redirectUrl\" value=\"https://login.xecurify.com/moas/initializepayment\" />\r\n" + 
							"			<input type=\"text\" name=\"requestOrigin\" id=\"requestOrigin\"  />\r\n" + 
							"		</form>\r\n" + 
							"        <!-- Navbar-->\r\n" + 
							"        <header class=\"app-header\"><a class=\"app-header__logo\" href=\"#\" style=\"margin-top:10px;\"><img src=\"?resource=/resources/images/logo-home.png\"></a>\r\n" + 
							"        <!-- Sidebar toggle button -->\r\n" + 
							"        <ul class=\"app-nav\">\r\n" + 
							"          <li class=\"dropdown\"><a class=\"app-nav__item\" href=\"#\" data-toggle=\"dropdown\" aria-label=\"Open Profile Menu\"><i class=\"fa fa-user fa-lg\"><span  style=\"margin-left:5px\">"+session.getAttribute("admin_email")+"</span><span style=\"padding-left:5px;\"><i class=\"fa fa-caret-down\"></i></span></i></a>\r\n" + 
							"            <ul class=\"dropdown-menu settings-menu dropdown-menu-right\">\r\n" + 
							"          <li><a class=\"dropdown-item\" href=\"?action=logout\"><i class=\"fa fa-sign-out fa-lg\"></i>Logout</a></li>\r\n" + 
							"        </ul>\r\n" + 
							"        </li>\r\n" + 
							"    </ul>\r\n" + 
							"        </header>\r\n" + 
							"        <!-- Sidebar menu-->\r\n" + 
							"    <div class=\"app-sidebar__overlay\" data-toggle=\"sidebar\"></div>\r\n" + 
							"    \r\n" + 
							"    <aside class=\"app-sidebar\">\r\n" + 
							"    <div class=\"app-sidebar__user\" style=\"padding-left:40px\"><img src=\"?resource=/resources/images/miniorange.png\"  style=\"width:37.25px; height:50px;\" alt=\"User Image\">\r\n" + 
							"        <div style=\"margin-left:15px;\">\r\n" + 
							"          <p class=\"app-sidebar__user-name\">JAVA SAML</p>\r\n" + 
							"          <p class=\"app-sidebar__user-designation\">Connector</p>\r\n" + 
							"        </div>\r\n" + 
							"      </div>\r\n" + 
							"      <ul class=\"app-menu\">\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=setup\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-gear\"></i><span class=\"app-menu__label\"><b>Plugin Settings</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=setupguide\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-info-circle\"></i><span class=\"app-menu__label\"><b>How to Setup?</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item active\" href=\"?action=licence\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-dollar\"></i><span class=\"app-menu__label\"><b>Licensing</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=support\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-support\"></i><span class=\"app-menu__label\"><b>Support</b></span></a></li>      </ul>\r\n" + 
							"    </aside>\r\n" + 
							"    <main class=\"app-content\">\r\n" + 
							"       <!-- <div class=\"app-title\">\r\n" + 
							"          <div>\r\n" + 
							"            <h1><i class=\"fa fa-dollar\"></i>  Licensing</h1>\r\n" + 
							"            \r\n" + 
							"          </div>\r\n" + 
							"          <ul class=\"app-breadcrumb breadcrumb\">\r\n" + 
							"            <li class=\"breadcrumb-item\"><i class=\"fa fa-home fa-lg\"></i></li>\r\n" + 
							"            <li class=\"breadcrumb-item\"><a href=\"#\">Licensing</a></li>\r\n" + 
							"          </ul>\r\n" + 
							"        </div>  --> \r\n" + 
							"\r\n" + 
							"      <p name=\"saml_message\"></p>\r\n" + 
							"        <div class=\"row\">\r\n" + 
							"          <div class=\"col-md-12\">\r\n" + 
							"            <div class=\"tile\">\r\n" + 
							"              <div class=\"row\">\r\n" + 
							"                <div class=\"col-lg-10\">\r\n" + 
							"                  <input type=\"hidden\" value=\"false\" id=\"mo_customer_registered\">\r\n" + 
							"                  <div id=\"pricing_container\" style=\"text-align:center; padding-bottom:30px;\" >\r\n" + 
							"                    <h3 style=\"margin-left:180px\"><b>Licensing Plans:</b></h3>\r\n" + 
							"                    <br>\r\n" + 
							"               			<div>\r\n" + 
							"					    <a id=\"pricing-table\"></a>\r\n" + 
							"					    <div class=\"container\">\r\n" + 
							"					        <div class=\"container\" style=\"margin-left:8em;width: 104% !important;font-family: open sans;font-size:14px\">\r\n" + 
							"					            <div class=\"columns wp-sso\">\r\n" + 
							"					                <ul class=\"price1\">\r\n" + 
							"					                    <li class=\"header premium\" style=\"padding-bottom:2.2em;\">\r\n" + 
							"					                        <h2>FREE</h2>\r\n" + 
							"					                        <br>\r\n" + 
							"					                        <sup id=\"dollar\"><span class=\"cd-currency\">$</span></sup>\r\n" + 
							"					                        <span class=\"cd-value\">0</span>\r\n" + 
							"					                      	<br>\r\n" + 
							"					                        <br>\r\n" + 
							"					                        <br>\r\n" + 
							"					                        <br>\r\n" + 
							"					                        \r\n" + 
							"					                        <span style=\"color:#2f6062;\"><b>You are using the free version of Java SAML Connector.</b></span>\r\n" + 
							"					                        <br>\r\n" + 
							"					                    </li>\r\n" + 
							"					                    \r\n" + 
							"					                    <li class=\"bold-features\" style=\"color:#2f6062;\"><strong>See the Free Plugin features list below</strong><br><br></li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Unlimited Authentications</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Configurable SP Base URL</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Custom Application URL</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Add Login link in your application</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Attribute mapping(Only NameID is allowed)</li>\r\n" + 
							"					                    <li>-</li>\r\n" + 
							"					                    <li>-</li>\r\n" + 
							"					                    <li>-</li>\r\n" + 
							"					                    <li>-</li>\r\n" + 
							"					                    <li>-</li>\r\n" + 
							"					                    <li>\r\n" + 
							"					                        &nbsp;<b>Support</b>\r\n" + 
							"					                        <p class=\"contactus-margin\"><a href=\"https://www.miniorange.com/contact\" target=\"_blank\" class=\"contactus-link\">Contact Us</a></p>\r\n" + 
							"					                    </li>\r\n" + 
							"					                </ul>\r\n" + 
							"					            </div>\r\n" + 
							"					            <div class=\"columns wp-sso\">\r\n" + 
							"					                <ul class=\"price\">\r\n" + 
							"					                    <li class=\"header standard-enterprise\" style=\"padding-bottom:3.8em;\">\r\n" + 
							"					                        <h2>PREMIUM</h2>\r\n" + 
							"					                        <br>\r\n" + 
							"					                        <div class=\"cd-price\">\r\n" + 
							"					                            <sup id=\"dollar\"><span class=\"cd-currency\">$</span></sup>\r\n" + 
							"					                            <span class=\"cd-value\">449<sup><span>*</span></sup> \r\n" + 
							"					                            </span>\r\n" + 
							"					                        </div>\r\n" + 
							"					                    </li>\r\n" + 
							"					                    <li class=\"upgrade-now\">\r\n" + 
							"					                        <a class=\"upgrade-now-link\" onclick=\"upgradeform('java_saml_premium_plan')\" style=\"color:#fff;\">Upgrade Now</a>\r\n" + 
							"					                    </li>\r\n" + 
							"					                    <li class=\"bold-features\" style=\"color:#2f6062;\"><strong>See the Premium Plugin features list below</strong><br><br></li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Unlimited Authentications</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Signed Request</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Signed Assertion and Response</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Configurable SAML request binding type</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Custom Attribute mapping</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">SAML Single Logout</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Force Authentication</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Custom Application URL</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Configurable SP Base URL</li>\r\n" + 
							"					                    <li class=\"medium-font-size\">Add Login link in your application</li>\r\n" + 
							"					                    <li>\r\n" + 
							"					                        &nbsp;<b>Support</b>\r\n" + 
							"					                        <p class=\"contactus-margin\"><a href=\"https://www.miniorange.com/contact\" target=\"_blank\" class=\"contactus-link\">Contact Us</a></p>\r\n" + 
							"					                    </li>\r\n" + 
							"					                </ul>\r\n" + 
							"					            </div>\r\n" + 
							"					        </div>\r\n" + 
							"					    </div>\r\n" + 
							"					</div>\r\n" + 
							"					</div>      \r\n" + 
							"                  </div>\r\n" + 
							"                </div>\r\n" + 
							"              </div>\r\n" + 
							"            </div>\r\n" + 
							"          </div>\r\n" + 
							"         </div>   "
							+ " </main>");
					html.append(htmlEnd);
					response.setContentType("text/html; charset=ISO-8859-1");
					response.setCharacterEncoding("ISO-8859-1");
					response.getOutputStream().write(html.toString().getBytes("UTF-8"));
				}
				else if(request.getParameter("action").equals("support")) {
					String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
					File f = new File(fileName);
					Properties p = new Properties();
					FileReader fr = new FileReader(f);
					p.load(fr);
					if(request.getParameter("choice")!=null && request.getParameter("choice").equals("mo_saml_contact_us_query_option")) {
						String email,phoneNumber,query;
						email = request.getParameter("mo_saml_contact_us_email");
						phoneNumber = request.getParameter("mo_saml_contact_us_phone");
						query = request.getParameter("mo_saml_contact_us_query");
						MoSupportHandler.submitSupportQuery(email,phoneNumber,query,p);
					}
					StringBuffer html = new StringBuffer();
					html.append(htmlStart);
					html.append("<ul class=\"app-menu\">\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=setup\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-gear\"></i><span class=\"app-menu__label\"><b>Plugin Settings</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=setupguide\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-info-circle\"></i><span class=\"app-menu__label\"><b>How to Setup?</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item\" href=\"?action=licence\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-dollar\"></i><span class=\"app-menu__label\"><b>Licensing</b></span></a></li>\r\n" + 
							"        <li><a class=\"app-menu__item active\" href=\"?action=support\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-support\"></i><span class=\"app-menu__label\"><b>Support</b></span></a></li>      \r\n" + 
							"      </ul>\r\n" + 
							"    </aside>");
					html.append("<main class=\"app-content\">"
							+ "<p name=\"saml_message\"></p>\r\n" + 
							"\r\n" + 
							"        <div class=\"row\">\r\n" + 
							"            <div class=\"col-md-12\">\r\n" + 
							"              <div class=\"tile\">\r\n" + 
							"               <form method=\"post\" action=\"\">\r\n" + 
							"                <div class=\"row\">\r\n" + 
							"                  <div class=\"col-lg-10\">\r\n" + 
							"                    \r\n" + 
							"                        <p><b>Need any help? We can help you in configuring the connector with your Identity Provider. Just send us a query and we will get back to you soon.</b></p>\r\n" + 
							"                        <input type=\"hidden\" name=\"choice\" value=\"mo_saml_contact_us_query_option\"/>\r\n" + 
							"                        <div class=\"form-group\">\r\n" + 
							"                            <input class=\"form-control\" type=\"text\" name=\"mo_saml_contact_us_email\" placeholder=\"Enter your email\" value=\"\">\r\n" + 
							"                        </div>\r\n" + 
							"                        <div class=\"form-group\">\r\n" + 
							"                            <input class=\"form-control\" type=\"tel\" name=\"mo_saml_contact_us_phone\"\r\n" + 
							"                            pattern=\"[\\+]\\d{11,14}|[\\+]\\d{1,4}[\\s]\\d{9,10}\" placeholder=\"Enter your phone number\">\r\n" + 
							"                        </div>\r\n" + 
							"                        <div class=\"form-group\">\r\n" + 
							"                        <textarea class=\"form-control\" name=\"mo_saml_contact_us_query\" placeholder=\"Enter your query here\"\r\n" + 
							"                        onkeypress=\"mo_saml_valid_query(this)\" onkeyup=\"mo_saml_valid_query(this)\"\r\n" + 
							"                        onblur=\"mo_saml_valid_query(this)\"></textarea>\r\n" + 
							"                        </div>\r\n" + 
							"\r\n" + 
							"                  </div>\r\n" + 
							"                </div>\r\n" + 
							"                <div class=\"tile-footer\">\r\n" + 
							"                    <button class=\"btn btn-primary\" type=\"submit\" name=\"submit\">Submit</button>\r\n" + 
							"                </div>\r\n" + 
							"                </form>\r\n" + 
							"              </div>\r\n" + 
							"            </div>\r\n" + 
							"        </div>\r\n" + 
							"    </main>\r\n" + 
							"    <script>\r\n" + 
							"        function mo_saml_valid_query(f) {\r\n" + 
							"            !(/^[a-zA-Z?,.\\(\\)\\/@ 0-9]*$/).test(f.value) ? f.value = f.value.replace(\r\n" + 
							"                /[^a-zA-Z?,.\\(\\)\\/@ 0-9]/, '') : null;\r\n" + 
							"        }\r\n" + 
							"    </script>");
					html.append(htmlEnd);
					response.setContentType("text/html; charset=ISO-8859-1");
					response.setCharacterEncoding("ISO-8859-1");
					response.getOutputStream().write(html.toString().getBytes("UTF-8"));
				}
			}
			else {
				if(request.getParameter("submit")!=null) {
					String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
					File f = new File(fileName);
					Properties p = new Properties();
					FileReader fr = new FileReader(f);
					p.load(fr);
					fr.close();
					String spBaseUrl = request.getParameter("spBaseUrl");
					Enumeration<String> paramNames = request.getParameterNames();
					while(paramNames.hasMoreElements()) {
						String key = paramNames.nextElement();
						String value = request.getParameter(key);
						if(key.equals("spEntityId")) {
							value = spBaseUrl.substring(0,spBaseUrl.length()-1);
						}
						else if(key.equals("acsUrl")) {
							value = spBaseUrl.concat("sso?action=acs");
						}
						p.setProperty(key,value);
					}
					FileWriter fw = new FileWriter(f);
					p.store(fw,null);
					fw.close();
				}

				String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
				File f = new File(fileName);
				Properties p = new Properties();
				if(f.exists()) {
					p.load(new FileReader(f));
				}
				else {
					f.createNewFile();
				}

				String idpName = "",
					   idpEntityId = "",
					   samlLoginUrl = "";
				if(!p.isEmpty()) {
					if(p.containsKey("idpName")) {
						idpName = p.getProperty("idpName");
					}
					if(p.containsKey("idpEntityId")) {
						idpEntityId = p.getProperty("idpEntityId");
					}
					if(p.containsKey("samlLoginUrl")) {
						samlLoginUrl = p.getProperty("samlLoginUrl");
					}
				}
				StringBuffer html = new StringBuffer();
				html.append(htmlStart);
				html.append("<ul class=\"app-menu\">\r\n" + 
						"        <li><a class=\"app-menu__item active\" href=\"?action=setup\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-gear\"></i><span class=\"app-menu__label\"><b>Plugin Settings</b></span></a></li>\r\n" + 
						"        <li><a class=\"app-menu__item\" href=\"?action=setupguide\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-info-circle\"></i><span class=\"app-menu__label\"><b>How to Setup?</b></span></a></li>\r\n" + 
						"        <li><a class=\"app-menu__item\" href=\"?action=licence\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-dollar\"></i><span class=\"app-menu__label\"><b>Licensing</b></span></a></li>\r\n" + 
						"        <li><a class=\"app-menu__item\" href=\"?action=support\"><i style=\"font-size:20px;\" class=\"app-menu__icon fa fa-support\"></i><span class=\"app-menu__label\"><b>Support</b></span></a></li>      \r\n" + 
						"      </ul>\r\n" + 
						"    </aside>"
						+ "<main class=\"app-content\">");
				if(request.getParameter("submit")!=null) {
	        		html.append("<div class=\"row\" style=\"background: #5bf5a3;border: darkgreen 2px solid;"
	        		+"margin: auto; margin-bottom: 20px;\">"
	        	   	+"<div style=\" font-size: 17px; padding: 8px;"
	        	    +"padding-left: 17px; \">Your Configuration has been saved.</div>"
	        	    +"</div>");
	        	}
				html.append("<div class=\"row\">\r\n" + 
						"            <div class=\"col-md-12\">\r\n" + 
						"             <form method=\"POST\" action=\"\" id=\"saml_form\">\r\n" +
						"             <input type=\"hidden\" name=\"appName\" value=\""+appName+"\">\r\n" +
						"              <div class=\"tile\">\r\n" + 
						"                <div class=\"row\">\r\n" + 
						"                  <div class=\"col-lg-6\">                  \r\n" + 
						"                    \r\n" + 
						"                      <h4>Identity Provider Settings</h4>\r\n" + 
						"                      <br>\r\n" + 
						"                      <div class=\"form-group\">\r\n" + 
						"                        <label for=\"idpName\"><b>Identity Provider Name</b></label>\r\n" + 
						"                        <input class=\"form-control\" name=\"idpName\" id=\"idpName\" type=\"text\"\r\n" + 
						"                        value=\""+idpName+"\">\r\n" + 
						"                      </div>\r\n" + 
						"                      <div class=\"form-group\">\r\n" + 
						"                        <label for=\"idpEntityId\"><b>IDP Entity ID</b></label>\r\n" + 
						"                        <input class=\"form-control\" name=\"idpEntityId\" id=\"idpEntityId\" type=\"text\"\r\n" + 
						"                        value=\""+idpEntityId+"\">\r\n" + 
						"                      </div>\r\n" + 
						"                      <div class=\"form-group\">\r\n" + 
						"                        <label for=\"samlLoginUrl\"><b>SAML Login URL</b></label>\r\n" + 
						"                        <input class=\"form-control\" name=\"samlLoginUrl\" id=\"samlLoginUrl\" type=\"text\"\r\n" + 
						"                        value=\""+samlLoginUrl+"\">\r\n" + 
						"                      </div>\r\n" + 
						"\r\n" + 
						"                      <label><b>SAML Login Binding type</b></label>\r\n" + 
						"                      <table>\r\n" + 
						"                        <tr>\r\n" + 
						"                          <td style=\"padding:10px;\">\r\n" + 
						"                      <div>\r\n" + 
						"                        <label>\r\n" + 
						"                          <input type=\"radio\" name=\"samlLoginBindingType\" id=\"http_redirect_binding\" value=\"HttpRedirect\" checked\r\n");

				html.append("                      ><span class=\"label-text\" style=\"margin-left:5px;\">Http-Redirect</span>    \r\n" + 
						"                        </label>\r\n" + 
						"                      </div>\r\n" + 
						"                      </td>\r\n" + 
						"                      <td style=\"padding:10px;\">\r\n" + 
						"                      <fieldset disabled=\"\">\r\n" + 
						"                      <div>\r\n" + 
						"                        <label>\r\n" + 
						"                        <input type=\"radio\" name=\"samlLoginBindingType\" id=\"http_post_binding\" value=\"HttpPost\"\r\n"); 

				html.append("                        ><span class=\"label-text\" style=\"margin-left:5px;\">Http-Post</span>\r\n" + 
						"                        </label>\r\n" + 
						"                        <small style=\"color:#FF0000\">Not available in TRIAL version</small>\r\n" + 
						"                      </div>\r\n" + 
						"                      </fieldset>\r\n" + 
						"                      </td>\r\n" + 
						"                      </tr>\r\n" + 
						"                    </table>\r\n" + 
						"\r\n" + 
						"                      <fieldset disabled=\"\">\r\n" + 
						"                      <div class=\"form-group\">\r\n" + 
						"                          <label for=\"samlLogoutUrl\"><b>SAML Logout URL</b></label>\r\n" + 
						"                          <input class=\"form-control\" name=\"samlLogoutUrl\" id=\"samlLogoutUrl\" type=\"text\" value=\"\">\r\n" + 
						"                          <small style=\"color:#FF0000\">Not available in TRIAL version</small>\r\n" + 
						"                        </div>\r\n" + 
						"                      </fieldset>\r\n" + 
						"                      <fieldset disabled=\"\">\r\n" + 
						"                      <div class=\"form-group\">\r\n" + 
						"                        <label for=\"x509Certificate\"><b>SAML x509 Certificate</b></label>\r\n" + 
						"                        <textarea class=\"form-control\" id=\"x509Certificate\" name=\"x509Certificate\" rows=\"5\">\r\n" + 
						"                        </textarea>\r\n" + 
						"                        <small style=\"color:#FF0000\">Not available in TRIAL version</small>\r\n" + 
						"                      </div>\r\n" + 
						"                      </fieldset>\r\n" + 
						"                      \r\n" + 
						"                      \r\n" + 
						"                      <fieldset disabled=\"\">\r\n" + 
						"                      <div>\r\n" + 
						"                        <label>\r\n" + 
						"                          <input type=\"checkbox\" id=\"signResponse\" name=\"signResponse\"\r\n"); 

				html.append("                          ><span class=\"label-text\" style=\"margin-left:5px;\">Signed Response</span>\r\n" + 
						"                        </label>&nbsp\r\n" + 
						"                        <small style=\"color:#FF0000\">Not available in TRIAL version</small>\r\n" + 
						"                      </div>\r\n" + 
						"                      </fieldset>\r\n" + 
						"\r\n" + 
						"                      <fieldset disabled=\"\">\r\n" + 
						"                      <div>\r\n" + 
						"                        <label>\r\n" + 
						"                          <input type=\"checkbox\" id=\"signAssertion\" name=\"signAssertion\"\r\n"); 

				html.append("                          ><span class=\"label-text\" style=\"margin-left:5px;\">Signed Assertion</span>\r\n" + 
						"                        </label>&nbsp\r\n" + 
						"                        <small style=\"color:#FF0000\">Not available in TRIAL version</small>\r\n" + 
						"                      </div>\r\n" + 
						"                      </fieldset>");
				html.append("</div>\r\n" + 
						"                  <div class=\"col-lg-4 offset-lg-1\">\r\n" + 
						"                    <h4>Service Provider Settings</h4>\r\n" + 
						"                    <br>\r\n" + 
						"                    <div class=\"form-group\">\r\n" + 
						"                        <label for=\"spBaseUrl\"><b>Base URL</b></label>\r\n" + 
						"                        <input class=\"form-control\" id=\"spBaseUrl\" name=\"spBaseUrl\" type=\"text\"\r\n"); 
				String baseUrl = "";
	            if(request.getParameter("spBaseUrl")!=null) {
	         	   baseUrl = request.getParameter("spBaseUrl");
	            }
	            else {
	         	   String actualUrl = request.getRequestURL().toString();
	         	   baseUrl = actualUrl.replace("sso","");
	            }
	            html.append("value=\""+baseUrl+"\"");
				html.append("                        >\r\n" + 
						"                    </div>\r\n" + 
						"                    \r\n" + 
						"                    <div class=\"form-group\">\r\n" + 
						"                      <label for=\"spEntityId\"><b>SP Entity ID</b></label>\r\n" + 
						"                      <input class=\"form-control\" id=\"spEntityId\" name=\"spEntityId\" type=\"text\" readonly=\"\"\r\n"); 
				String entityId = baseUrl.substring(0,baseUrl.length()-1);
	          	html.append("value=\""+entityId+"\"");
				html.append("                      >\r\n" + 
						"                    </div>\r\n" + 
						"\r\n" + 
						"                    <div class=\"form-group\">\r\n" + 
						"                      <label for=\"acsUrl\"><b>ACS URL</b></label>\r\n" + 
						"                      <input class=\"form-control\" id=\"acsUrl\" name=\"acsUrl\" type=\"text\" readonly=\"\"\r\n");
				String acs_Url = entityId.concat("/sso?action=acs");
	          	html.append("value=\""+acs_Url+"\""); 
				html.append("                      >\r\n" + 
						"                    </div>\r\n" + 
						"\r\n" + 
						"                    <br><br>\r\n" + 
						"                    <h4>Attribute Mapping</h4>\r\n" + 
						"                    <br>\r\n" + 
						"                    <fieldset disabled=\"\">\r\n" + 
						"                    <div class=\"form-group\">\r\n" + 
						"                      <label for=\"amEmail\"><b>EMAIL</b></label>\r\n" + 
						"                      <input class=\"form-control\" id=\"amEmail\" name=\"amEmail\" type=\"text\" value=\"NameID\">\r\n" + 
						"                      <small style=\"color:#FF0000\">Not configurable in TRIAL version</small>\r\n" + 
						"                    </div>\r\n" + 
						"                    </fieldset>\r\n" + 
						"\r\n" + 
						"                    <fieldset disabled=\"\">\r\n" + 
						"                    <div class=\"form-group\">\r\n" + 
						"                      <label for=\"amUsername\"><b>Username</b></label>\r\n" + 
						"                      <input class=\"form-control\" id=\"amUsername\" name=\"amUsername\" type=\"text\" value=\"NameID\">\r\n" + 
						"                      <small style=\"color:#FF0000\">Not configurable in TRIAL version</small>\r\n" + 
						"                    </div>\r\n" + 
						"                    </fieldset>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                  </div>\r\n" + 
						"                </div>\r\n" + 
						"                <div class=\"tile-footer\">\r\n" + 
						"                    <button class=\"btn btn-primary\" type=\"submit\" name=\"submit\" id=\"submit\" style=\"margin-right:10px;\">Save</button>\r\n" + 
						"                    <a target=\"_blank\" href=\"?action=login&RelayState=testconfig\" style=\"text-decoration:none\"><button class=\"btn btn-primary\" name=\"do_sso\" type=\"button\">Test Configuration</button></a>\r\n" + 
						"                  </div>\r\n" + 
						"              </div>\r\n" + 
						"            </form>\r\n" + 
						"            </div>\r\n" + 
						"        </div>\r\n" + 
						"     \r\n" + 
						"    </main>\r\n");
				html.append(htmlEnd);
				response.setContentType("text/html; charset=ISO-8859-1");
				response.setCharacterEncoding("ISO-8859-1");
				response.getOutputStream().write(html.toString().getBytes("UTF-8"));
			}
		}
		else if(request.getParameterMap().containsKey("action") && !request.getParameterMap().containsKey("resource")) {
			if(request.getParameter("action").equals("login")) {
				String fileName = getServletContext().getRealPath("WEB-INF/config.properties");
				File f = new File(fileName);
				Properties p = new Properties();
				FileReader fr = new FileReader(f);
				p.load(fr);
				settings = new MoSAMLSettings(p);
				manager = new MoSAMLManager();
				String relayState = "";
				if(request.getParameter("RelayState")!=null && request.getParameter("RelayState").equals("testconfig")) {
					relayState = "testconfig";
				}
				else if(request.getParameter("redirectto")!=null) {
					relayState = request.getParameter("redirectto");
				}
				else {
					relayState = settings.getSpBaseUrl();
				}
				manager.createAuthnRequestAndRedirect(request, response, relayState, settings);
			}
			else if(request.getParameter("action").equals("acs")) {
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
					
					if(relayState.equals("testconfig")) {
						response.getWriter().print(new MoSAMLTestResult(samlResponse.getAttributes(),null,p).execute());
					}
					else {
						String[] nameIDValue = samlResponse.getAttributes().get("NameID");
						if(nameIDValue[0].isEmpty()) {
							request.getRequestDispatcher("sso?action=invalidresponse").forward(request, response);
						}
						else {
							session.setAttribute("email",nameIDValue[0]);
							String applicationUrl = settings.getApplicationUrl();
							if(!relayState.equals(settings.getSpBaseUrl())) {
								response.sendRedirect(relayState);
							}
							else if(applicationUrl!=null) {
								response.sendRedirect(settings.getApplicationUrl());
							}
							else {
								response.getWriter().print("<html>"
										+ "<body>"
										+ "You have been logged in.<br>"
										+ "If your want to redirect to a different URL after login, configure the Redirect Endpoint in step 5 of <b>How to Setup?</b> tab of the connector."
										+ "</body>"
										+ "</html>");
							}
						}
					}
					
				} catch(MoSAMLException e) {
					request.setAttribute("message",e.getMessage());
					request.setAttribute("resolution",e.getResolution());
					request.setAttribute("errorcode",e.getErrorCode());
					request.getRequestDispatcher("sso?action=error").forward(request, response);
				}
			}
			else if(request.getParameter("action").equals("error")) {
				String html = "<!DOCTYPE html>\r\n" + 
						"<html>\r\n" + 
						"<body>\r\n" + 
						"	<div style=\"font-family:Calibri;padding:0 3%;\">\r\n" + 
						"		<div style=\"color: #a94442;background-color: #f2dede;padding: 15px;margin-bottom: 20px;text-align:center;border:1px solid #E6B3B2;font-size:18pt;\"> ERROR</div>\r\n" + 
						"            <div style=\"color: #a94442;font-size:14pt; margin-bottom:20px;\">\r\n" + 
						"            	<p><strong>Error : "+request.getAttribute("message")+"</strong></p>\r\n" + 
						"				<p>"+request.getAttribute("resolution")+"</p>\r\n" + 
						"                \r\n" + 
						"			</div>\r\n" + 
						"	</div>		\r\n" + 
						"</body>\r\n" + 
						"</html>";
				response.setContentType("text/html; charset=ISO-8859-1");
				response.setCharacterEncoding("ISO-8859-1");
				response.getOutputStream().write(html.toString().getBytes("UTF-8"));
			}
			else if(request.getParameter("action").equals("invalidresponse")) {
				String html="<!DOCTYPE html>\r\n" + 
						"<html>\r\n" + 
						"<body>\r\n" + 
						"	<div style=\"font-family:Calibri;padding:0 3%;\">\r\n" + 
						"		<div style=\"color: #a94442;background-color: #f2dede;padding: 15px;margin-bottom: 20px;text-align:center;border:1px solid #E6B3B2;font-size:18pt;\"> ERROR</div>\r\n" + 
						"            <div style=\"color: #a94442;font-size:14pt; margin-bottom:20px; text-align:center\">\r\n" + 
						"            	<p><strong>Invalid SAML Response received. Please try again.</strong></p>\r\n" + 
						"			</div>\r\n" + 
						"	</div>	\r\n" + 
						"</body>\r\n" + 
						"</html>";
				response.setContentType("text/html; charset=ISO-8859-1");
				response.setCharacterEncoding("ISO-8859-1");
				response.getOutputStream().write(html.toString().getBytes("UTF-8"));
			}
		}
		else if(request.getParameterMap().containsKey("resource")) {
			String path = request.getParameter("resource");
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public boolean isUserRegistered() {
		String fileName = getServletContext().getRealPath("WEB-INF/credentials.properties");
		File f = new File(fileName);
		if(f.exists()) {
			if(f.length() == 0) {
				return false;
			}
			else {
				return true;
			}
		}		
		else {
			return false;
		}
	}
	
	private void setContentType(String path, HttpServletResponse response) {
        if (path.toLowerCase().endsWith(".css")) {
            response.setContentType("text/css");
        }
        else if (path.toLowerCase().endsWith(".js")) {
            response.setContentType("text/javascript");
        }
        else if (path.toLowerCase().endsWith(".png")) {
        	response.setContentType("image/png");
        }
    }
}
