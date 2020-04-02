package com.miniorange.app.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;

public class MoSupportHandler extends HttpServlet 
{
	private static MoSAMLSettings settings;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String AUTH_BASE_URL = "https://login.xecurify.com/moas";
	public static final String SUPPORT_QUERY_URL = AUTH_BASE_URL + "/rest/customer/contact-us";
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
		
		email = request.getParameter("mo_saml_contact_us_email");
		mobileNumber = request.getParameter("mo_saml_contact_us_phone");
		message = request.getParameter("mo_saml_contact_us_query");
//		String result = submitSupportQuery(email, mobileNumber, message);
		
		//response.sendRedirect("support.jsp");
		//response.setContentType("text/html");	
//		PrintWriter out = response.getWriter();
//		out.print(result);
		
		/*String spBaseURL = request.getRequestURL().toString().replace(request.getRequestURI(),"");
		spBaseURL = spBaseURL + "/admin/settings?category=miniorangesaml";
		
		out.print("<html><head><title>Submit Query</title></head><body><script type='text/javascript'> alert(\""+result+"\");"
		+"window.location.href =\""+spBaseURL+"\";"
		+"</script></body></html>");*/
		
	}
	
	private static HashMap<String, String> getAuthorizationHeaders(Long customerId, String apiKey) {
		HashMap<String, String> headers = new HashMap<String, String>();
		Long timestamp = System.currentTimeMillis();
		String stringToHash = customerId + timestamp + apiKey;
		String hashValue = DigestUtils.sha512Hex(stringToHash);

		headers.put("Customer-Key", String.valueOf(customerId));
		headers.put("Timestamp", String.valueOf(timestamp));
		headers.put("Authorization", hashValue);
		
		
		return headers;
	}
	
	
	
	public static String submitSupportQuery(String email, String phone, String query, Properties p) {
		String response="";
		
		settings = new MoSAMLSettings(p);
		
		try {
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("company", settings.getSpBaseUrl());
			jsonObject.addProperty("email", email);
			jsonObject.addProperty("phone", phone);
		
			jsonObject.addProperty("query", StringUtils.prependIfMissing(query, "[JAVA SAML Connector] "));
			
			String json = jsonObject.toString();
			
			response = MoSupportUtils.sendPostRequest(
					SUPPORT_QUERY_URL, json,
					CONTENT_TYPE_JSON, 
					getAuthorizationHeaders(Long.valueOf(DEFAULT_CUSTOMER_KEY),DEFAULT_API_KEY));
			
			
			
			
			
	
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return response;
	}

}
