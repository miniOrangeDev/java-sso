package com.miniorange.app.helpers;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.*;

import com.miniorange.app.classes.MoSAMLException;
import com.miniorange.app.classes.MoSAMLResponse;

import java.util.*;


public class MoSAMLManager {
	private long timediff;
	private String replacement="";
	
	public void createAuthnRequestAndRedirect(HttpServletRequest request, HttpServletResponse response,
            String relayState,MoSAMLSettings settings) {
		try {
			MoSAMLUtils.doBootstrap();
			AuthnRequest authnRequest = MoSAMLUtils.buildAuthnRequest(settings.getSpEntityId(),
					settings.getAcsUrl(), settings.getSamlLoginUrl(), settings.getNameIDFormat());
			String encodedAuthnRequest = MoSAMLUtils.base64EncodeRequest(authnRequest, false);
			String redirectUrl = StringUtils.EMPTY;
			redirectUrl = createUnSignedRedirectURL(settings.getSamlLoginUrl(), encodedAuthnRequest,
							relayState, false);
			httpRedirect(response, redirectUrl);			
		} catch (Throwable t) {
			t.printStackTrace();
			throw new MoSAMLException(MoSAMLException.SAMLErrorCode.UNKNOWN);
		}
	}
	
	private String createRequestQueryParamsForSignature(String httpRedirectRequest, String relayState)
			throws UnsupportedEncodingException {
		StringBuffer urlForSignature = new StringBuffer();
		urlForSignature.append(MoSAMLUtils.SAML_REQUEST_PARAM).append("=")
				.append(URLEncoder.encode(httpRedirectRequest, StandardCharsets.UTF_8.toString()));
		urlForSignature.append("&").append(MoSAMLUtils.RELAY_STATE_PARAM).append("=");
		if (StringUtils.isNotBlank(relayState)) {
			urlForSignature.append(URLEncoder.encode(relayState, StandardCharsets.UTF_8.toString()));
		} else {
			urlForSignature.append(URLEncoder.encode("/", StandardCharsets.UTF_8.toString()));
		}
		return urlForSignature.toString();
	}
	
	private String createResponseQueryParamsForSignature(String httpRedirectResponse, String relayState)
			throws UnsupportedEncodingException {
		StringBuffer urlForSignature = new StringBuffer();
		urlForSignature.append(MoSAMLUtils.SAML_RESPONSE_PARAM).append("=")
				.append(URLEncoder.encode(httpRedirectResponse, StandardCharsets.UTF_8.toString()));
		urlForSignature.append("&").append(MoSAMLUtils.RELAY_STATE_PARAM).append("=");
		if (StringUtils.isNotBlank(relayState)) {
			urlForSignature.append(URLEncoder.encode(relayState, StandardCharsets.UTF_8.toString()));
		} else {
			urlForSignature.append(URLEncoder.encode("/", StandardCharsets.UTF_8.toString()));
		}
		return urlForSignature.toString();
	}
	
	private String createUnSignedRedirectURL(String url, String samlRequestOrResponse, String relayState,
            Boolean isResponse) throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder(url);
		if (StringUtils.contains(url, "?") && !(StringUtils.endsWith(url, "?") || StringUtils.endsWith(url, "&"))) {
			builder.append("&");
		} else if (!StringUtils.contains(url, "?")) {
			builder.append("?");
		}
		if (isResponse) {
			builder.append(createResponseQueryParamsForSignature(samlRequestOrResponse, relayState));
		} else {
			builder.append(createRequestQueryParamsForSignature(samlRequestOrResponse, relayState));
		}
		return builder.toString();
	}
	

	public static void httpRedirect(HttpServletResponse response, String redirectUrl) throws IOException {
		response.sendRedirect(redirectUrl);
	}
	
	public MoSAMLResponse readSAMLResponse(HttpServletRequest request, HttpServletResponse response,MoSAMLSettings settings) {
		try {
			MoSAMLUtils.doBootstrap();
			String encodedSAMLResponse = request.getParameter(MoSAMLUtils.SAML_RESPONSE_PARAM);
			String relayState = request.getParameter(MoSAMLUtils.RELAY_STATE_PARAM);
			Response samlResponse = MoSAMLUtils.decodeResponse(encodedSAMLResponse);
			if (!StringUtils.equals(samlResponse.getStatus().getStatusCode().getValue(), StatusCode.SUCCESS_URI)) {
				String message = StringUtils.EMPTY;
				if (samlResponse.getStatus().getStatusMessage() != null) {
					message = samlResponse.getStatus().getStatusMessage().getMessage()
							+ ". Status Code received in SAML response: "
							+ samlResponse.getStatus().getStatusCode().getValue().split(":")[7];
				} else {
					message = "Invalid status code \""
							+ samlResponse.getStatus().getStatusCode().getValue().split(":")[7]
							+ "\" received in SAML response";
				}

				if(StringUtils.equalsIgnoreCase(samlResponse.getStatus().getStatusCode().getValue().split(":")[7], StatusCode.RESPONDER_URI)){
					throw new MoSAMLException(message, MoSAMLException.SAMLErrorCode.RESPONDER);
				}
				else{
					throw new MoSAMLException(message, MoSAMLException.SAMLErrorCode.INVALID_SAML_STATUS);
				}
			}
			Assertion assertion;
			if (samlResponse.getAssertions() != null && samlResponse.getAssertions().size() > 0) {
				assertion = samlResponse.getAssertions().get(0);
			} else {
				// Encrypted Assertion is not supported in free plugin
				MoSAMLException.SAMLErrorCode errorCode = MoSAMLException.SAMLErrorCode.ENCRYPTED_ASSERTION;
				MoSAMLException e = new MoSAMLException(errorCode);
				throw e;

			}

			verifyConditions(assertion, settings.getSpEntityId());

			String acs = settings.getAcsUrl();
			String idpACS = acs+"?idp="+ "";
			verifyIssuer(samlResponse, assertion, settings.getIdpEntityId());
			verifyDestination(samlResponse, acs, idpACS);
			verifyRecipient(assertion, acs,idpACS);

			Map<String, String[]> attributes = getAttributes(assertion);
			NameID nameId = assertion.getSubject().getNameID();
			String nameIdValue = StringUtils.EMPTY;
			String sessionIndex = assertion.getAuthnStatements().get(0).getSessionIndex();
			if (nameId != null) {
				nameIdValue = nameId.getValue();
			}
			attributes.put("NameID", new String[] { nameIdValue });
			MoSAMLResponse samlResponseObj = new MoSAMLResponse(attributes, nameIdValue, sessionIndex, relayState);
			return samlResponseObj;
		} catch (MoSAMLException e) {
			throw e;
		} catch (Throwable e) {
			throw new MoSAMLException(e, MoSAMLException.SAMLErrorCode.UNKNOWN);
		}
	}
	
	public long timeInMiliseconds() {
		String time = "0";
		long timeDelay = Long.valueOf(time);
		timeDelay = timeDelay * 60 * 1000;
		return timeDelay;
	}
	
	private void verifyConditions(Assertion assertion, String audienceExpected) {
		Date now = new DateTime().toDate();
		Date notBefore = null;
		Date notOnOrAfter = null;
		long timeDifferenceInBefore = 0;
		long timeDifferenceInAfter = 0;
		if (assertion.getConditions().getNotBefore() != null) {
			notBefore = assertion.getConditions().getNotBefore().toDate();
			if (now.before(notBefore))
				timeDifferenceInBefore = Math.abs(notBefore.getTime() - now.getTime());
		}
		if (assertion.getConditions().getNotOnOrAfter() != null) {
			notOnOrAfter = assertion.getConditions().getNotOnOrAfter().toDate();
			if (now.after(notOnOrAfter))
				timeDifferenceInAfter = Math.abs(now.getTime() - notOnOrAfter.getTime());
		}
		long userAddeddelay = timeInMiliseconds();
		long timediff1=userAddeddelay - timeDifferenceInBefore;
		long timediff2=userAddeddelay - timeDifferenceInAfter;

		if(timediff1!=0) {
			timediff=-timediff1;
			replacement="Forward";
		}
		else
		{
			timediff=-timediff2;
			replacement="Back";
		}
		long valueinminutes=((timediff/(60*1000))%60);
		long Exactvalueinminutes=Math.incrementExact(valueinminutes);
		if (notBefore != null && now.before(notBefore) && userAddeddelay - timeDifferenceInBefore < 0) {
			//MoSAMLException e = new MoSAMLException(MoSAMLException.SAMLErrorCode.INVALID_CONDITIONS);
			//LOGGER.error("Received an assertion that is valid in the future. Check clock synchronization on IdP and "
			//		+ "SP.", e);
			//throw e;
			MoSAMLException.SAMLErrorCode errorCode = MoSAMLException.SAMLErrorCode.INVALID_CONDITIONS;
			MoSAMLException samlexception = new MoSAMLException(errorCode.getMessage(),
					TimeDiff(errorCode, Exactvalueinminutes), errorCode);

			throw samlexception;
		} else if (notOnOrAfter != null && (now.after(notOnOrAfter) || now.equals(notOnOrAfter))
				&& userAddeddelay - timeDifferenceInAfter < 0) {
			//MoSAMLException e = new MoSAMLException(MoSAMLException.SAMLErrorCode.INVALID_CONDITIONS);
			//LOGGER.error("Received an assertion with a session that has expired. Check clock synchronization on IdP "
			//		+ "and SP.", e);
			//throw e;
			MoSAMLException.SAMLErrorCode errorCode = MoSAMLException.SAMLErrorCode.INVALID_CONDITIONS;
			MoSAMLException samlexception = new MoSAMLException(errorCode.getMessage(),
					TimeDiff(errorCode, Exactvalueinminutes), errorCode);

			throw samlexception;
		}

		List<Audience> audiencesInAssertion = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences();
		Iterator<Audience> it = audiencesInAssertion.iterator();
		while (it.hasNext()) {
			Audience audience = it.next();
			if (StringUtils.equalsIgnoreCase(audience.getAudienceURI(), audienceExpected)) {
				return;
			}
		}
		MoSAMLException e = new MoSAMLException(MoSAMLException.SAMLErrorCode.INVALID_AUDIENCE);
		throw e;
	}
	
	private String TimeDiff(MoSAMLException.SAMLErrorCode error,long temp)
	{
		StringBuffer errorMsg = new StringBuffer(error.getResolution());
		//errorMsg.append(replacement);
		errorMsg.append(" Set your Server clock "+replacement+" by ");
		errorMsg.append(temp);
		errorMsg.append(" minutes  Or you can Increase ");
		errorMsg.append(temp);
		errorMsg.append(" minutes in  validate Saml Response in SSO setting tab.");
		return errorMsg.toString();
	}
	
	private void verifyIssuer(Response response, Assertion assertion, String idpEntityId) {
		String issuerInResponse = response.getIssuer().getValue();
		String issuerInAssertion = assertion.getIssuer().getValue();
		if (!StringUtils.equals(issuerInResponse, idpEntityId)) {
			MoSAMLException.SAMLErrorCode errorCode = MoSAMLException.SAMLErrorCode.INVALID_ISSUER;
			MoSAMLException e = new MoSAMLException(errorCode.getMessage(),
					buildResolutionMessage(errorCode, idpEntityId, issuerInResponse), errorCode);
			throw e;
		}
		if (!StringUtils.equals(issuerInAssertion, idpEntityId)) {
			MoSAMLException.SAMLErrorCode errorCode = MoSAMLException.SAMLErrorCode.INVALID_ISSUER;
			MoSAMLException e = new MoSAMLException(errorCode.getMessage(),
					buildResolutionMessage(errorCode, idpEntityId, issuerInAssertion), errorCode);
			throw e;	
		}
	}

	private void verifyDestination(Response response, String acsUrl, String idpAcsUrl) {
		// Destination is Optional field so verify only if exist.
		String destInResponse = response.getDestination();
		if (StringUtils.isBlank(destInResponse) || StringUtils.equals(destInResponse, acsUrl) || StringUtils.equals(destInResponse,idpAcsUrl)) {
			return;
		}
		MoSAMLException.SAMLErrorCode errorCode = MoSAMLException.SAMLErrorCode.INVALID_DESTINATION;
		MoSAMLException e = new MoSAMLException(errorCode.getMessage(),
				buildResolutionMessage(errorCode, acsUrl, destInResponse), errorCode);
		throw e;
	}

	private void verifyRecipient(Assertion assertion, String acsUrl, String idpAcsUrl) {
		String recipientInResponse = assertion.getSubject().getSubjectConfirmations().get(0)
				.getSubjectConfirmationData().getRecipient();
		if (StringUtils.isBlank(recipientInResponse) || StringUtils.equals(recipientInResponse, acsUrl) || StringUtils.equals(recipientInResponse,idpAcsUrl)) {
			return;
		}
		MoSAMLException.SAMLErrorCode errorCode = MoSAMLException.SAMLErrorCode.INVALID_RECIPIENT;
		MoSAMLException e = new MoSAMLException(errorCode.getMessage(),
				buildResolutionMessage(errorCode, acsUrl, recipientInResponse), errorCode);
		throw e;
	}
	
	private String buildResolutionMessage(MoSAMLException.SAMLErrorCode error, String found, String expected) {
		StringBuffer errorMsg = new StringBuffer(error.getResolution());
		errorMsg.append(" Add-on was expecting \"");
		errorMsg.append(expected);
		errorMsg.append("\" but found value is \"");
		errorMsg.append(found);
		errorMsg.append("\"");
		return errorMsg.toString();
	}
	
	
	private Map<String, String[]> getAttributes(Assertion assertion) {
		Map<String, String[]> attributes = new HashMap<String, String[]>();
		if (assertion.getAttributeStatements().size() > 0) {
			for (Attribute attr : assertion.getAttributeStatements().get(0).getAttributes()) {
				if (attr.getAttributeValues().size() > 0) {
					String[] values = new String[attr.getAttributeValues().size()];
					for (int i = 0; i < attr.getAttributeValues().size(); i++) {
						values[i] = attr.getAttributeValues().get(i).getDOM().getTextContent();
					}
					attributes.put(attr.getName(), values);
				}
			}
		}
		return attributes;
	}

}
