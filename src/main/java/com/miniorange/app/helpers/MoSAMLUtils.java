package com.miniorange.app.helpers;

import java.io.*;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.core.impl.*;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MoSAMLUtils {
	public static final String SAML_REQUEST_PARAM = "SAMLRequest";
	public static final String RELAY_STATE_PARAM = "RelayState";
	public static final String SAML_RESPONSE_PARAM = "SAMLResponse";

	private static boolean bootstrap = false;

	public static void doBootstrap() {
		if (!bootstrap) {
			try {
				bootstrap = true;
				DefaultBootstrap.bootstrap();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static AuthnRequest buildAuthnRequest(String issuer, String acsUrl, String destination, String nameIdFormat) {
		AuthnRequest authnRequest = new AuthnRequestBuilder().buildObject(SAMLConstants.SAML20P_NS,
				AuthnRequest.DEFAULT_ELEMENT_LOCAL_NAME, "samlp");
		DateTime issueInstant = new DateTime();
		authnRequest.setID(generateRandomString());
		authnRequest.setVersion(SAMLVersion.VERSION_20);
		authnRequest.setIssueInstant(issueInstant);
		authnRequest.setProtocolBinding(SAMLConstants.SAML2_POST_BINDING_URI);
		authnRequest.setIssuer(buildIssuer(issuer));
		authnRequest.setAssertionConsumerServiceURL(acsUrl);
		authnRequest.setDestination(destination);
		//Create NameIDPolicy
	    NameIDPolicyBuilder nameIdPolicyBuilder = new NameIDPolicyBuilder();
	    NameIDPolicy nameIdPolicy = nameIdPolicyBuilder.buildObject();
	    nameIdPolicy.setFormat(nameIdFormat);
	    nameIdPolicy.setAllowCreate(true);
	    //authnRequest.setNameIDPolicy(nameIdPolicy);
		return authnRequest;
	}
	
	public static String base64EncodeRequest(XMLObject request, Boolean isHttpPostBinding) throws Exception {
		Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(request);
		Element authDOM = marshaller.marshall(request);

		// DOM to string
		StringWriter requestWriter = new StringWriter();
		XMLHelper.writeNode(authDOM, requestWriter);
		String requestMessage = requestWriter.toString();

		if (isHttpPostBinding) {
			String authnRequestStr = Base64.encodeBytes(requestMessage.getBytes(), Base64.DONT_BREAK_LINES);
			return authnRequestStr;
		}
		// compressing
		Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
		deflaterOutputStream.write(requestMessage.getBytes());
		deflaterOutputStream.close();
		byteArrayOutputStream.close();
		String encodedRequestMessage = Base64.encodeBytes(byteArrayOutputStream.toByteArray(), Base64.DONT_BREAK_LINES);
		return encodedRequestMessage;
	}
	
	
	private static Issuer buildIssuer(String issuerValue) {
		Issuer issuer = new IssuerBuilder().buildObject(SAMLConstants.SAML20_NS, Issuer.DEFAULT_ELEMENT_LOCAL_NAME,
				"saml");
		issuer.setValue(issuerValue);
		return issuer;
	}
	
	public static String generateRandomString() {
		String uuid = UUID.randomUUID().toString();
		return "_" + StringUtils.remove(uuid, '-');
	}
	
	public static Response decodeResponse(String encodedResponse) throws Exception {
		String xml = new String(Base64.decode(encodedResponse), "UTF-8");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		/** Ignore comments while reading Response : XML Canonicalization Vulnerability  : https://www.kb.cert.org/vuls/id/475445 */
		documentBuilderFactory.setIgnoringComments(true);
		DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
		ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		Document document = docBuilder.parse(is);
		Element element = document.getDocumentElement();
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
		XMLObject xmlObj = unmarshaller.unmarshall(element);
		Response response = (Response) xmlObj;
		return response;
	}
}
