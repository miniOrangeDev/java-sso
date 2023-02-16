package com.miniorange.app.classes;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class MoSAMLTestResult {
    private Map<String, String[]> attributes;
    private MoSAMLException samlException;
    private boolean hasExceptionOccurred;
    private String nameId;
    private Properties properties;

    private String template = "<div style=\"font-family:Calibri;padding:0 3%;\">{{header}}{{commonbody}}{{footer}}</div>";
    private String successHeader = "<div style=\"color: #3c763d;background-color: #dff0d8; padding:2%;margin-bottom:20px;text-align:center;"
            + "border:1px solid #AEDB9A; font-size:18pt;\">TEST SUCCESSFUL</div>"
            + "<div style=\"display:block;text-align:center;margin-bottom:4%;\">"
            + "<img style=\"width:15%;\" src=\"{{right}}\"></div>";

    private String errorHeader = "<div style=\"color: #a94442;background-color: #f2dede;padding: 15px;margin-bottom: 20px;text-align:center;"
            + "border:1px solid #E6B3B2;font-size:18pt;\">TEST FAILED</div>"
            + "<div style=\"display:block;text-align:center;margin-bottom:4%;\">"
            + "<img style=\"width:15%;\"src=\"{{wrong}}\"></div>";

    private String commonBody = "<span style=\"font-size:14pt;\"><b>Hello</b>, {{email}}</span><br/>"
            + "<p style=\"font-weight:bold;font-size:14pt;margin-left:1%;\">ATTRIBUTES RECEIVED:</p>"
            + "<table style=\"border-collapse:collapse;border-spacing:0; display:table;width:100%;"
            + "font-size:14pt;background-color:#EDEDED;\">"
            + "<tr style=\"text-align:center;\">"
            + "        <td style=\"font-weight:bold;border:2px solid #949090;padding:2%;\">ATTRIBUTE NAME</td>"
            + "        <td style=\"font-weight:bold;padding:2%;border:2px solid #949090; word-wrap:break-word;\">ATTRIBUTE VALUE</td>"
            + "</tr>{{tablecontent}}"
            + "</table>";

    private String exceptionBody = "<div style=\"margin: 10px 0;padding: 12px;color: #D8000C;background-color: #FFBABA;font-size: 16px;"
            + "line-height: 1.618;\">{{exceptionmessage}}</div>";

    private String certError = "<p style=\"font-weight:bold;font-size:14pt;margin-left:1%;\">CERT CONFIGURED IN PLUGIN:</p><div style=\"color: #373B41;"
            + "font-family: Menlo,Monaco,Consolas,monospace;direction: ltr;text-align: left;white-space: pre;"
            + "word-spacing: normal;word-break: normal;font-size: 13px;font-style: normal;font-weight: 400;"
            + "height: auto;line-height: 19.5px;border: 1px solid #ddd;background: #fafafa;padding: 1em;"
            + "margin: .5em 0;border-radius: 4px;\">{{certinplugin}}</div>"
            + "<p style=\"font-weight:bold;font-size:14pt;margin-left:1%;\">CERT FOUND IN RESPONSE:</p><div style=\"color: #373B41;"
            + "font-family: Menlo,Monaco,Consolas,monospace;direction: ltr;text-align: left;white-space: pre;"
            + "word-spacing: normal;word-break: normal;font-size: 13px;font-style: normal;font-weight: 400;"
            + "height: auto;line-height: 19.5px;border: 1px solid #ddd;background: #fafafa;padding: 1em;"
            + "margin: .5em 0;border-radius: 4px;\">{{certfromresponse}}</div>";

    private String samlResponse = "<p style=\"font-weight:bold;font-size:14pt;margin-left:1%;\">SAML RESPONSE FROM IDP:</p><div style=\"color: #373B41;"
            + "font-family: Menlo,Monaco,Consolas,monospace;direction: ltr;text-align: left;white-space: pre;"
            + "word-spacing: normal;word-break: normal;font-size: 13px;font-style: normal;font-weight: 400;"
            + "height: auto;line-height: 19.5px;border: 1px solid #ddd;background: #fafafa;padding: 1em;"
            + "margin: .5em 0;border-radius: 4px;overflow:scroll\">{{samlresponse}}</div>";

    private String footer = "<div style=\"margin:3%;display:block;text-align:center;\">"
            + "<input style=\"padding:1%;width:100px;background: #0091CD none repeat scroll 0% 0%;cursor: pointer;"
            + "    font-size:15px;border-width: 1px;border-style: solid;border-radius: 3px;white-space: nowrap;"
            + "    box-sizing: border-box;border-color: #0073AA;box-shadow: 0px 1px 0px rgba(120, 200, 230, 0.6) inset;"
            + "    color: #FFF;\"type=\"button\" value=\"Done\" onClick=\"self.close();\"></div>";

    private String tableContent = "<tr><td style=\"font-weight:bold;border:2px solid #949090;padding:2%;\">{{key}}</td><td style=\"padding:2%;"
            + "border:2px solid #949090; word-wrap:break-word;\">{{value}}</td></tr>";

    public MoSAMLTestResult(Map<String, String[]> attributes, MoSAMLException samlException, Properties properties) {
        this.attributes = attributes;
        if (attributes.containsKey("NameID")) {
            this.nameId = attributes.get("NameID")[0];
        }
        this.samlException = samlException;
        this.hasExceptionOccurred = samlException == null ? false : true;
        this.properties = properties;
    }

    public String execute() throws IOException {
        processTemplateHeader();
        if (hasExceptionOccurred) {
            processExceptionTemplate();
        } else {
            processTemplateContent();
        }
        processTemplateFooter();
        return template;
    }

    private void processTemplateHeader() throws IOException {
        String header;
        if (nameId.isEmpty()) {
            header = errorHeader;
            header = header.replace("{{wrong}}", getImageURL("wrong.png"));
        } else {
            header = successHeader;
            header = header.replace("{{right}}", getImageURL("right.png"));
        }
        template = template.replace("{{header}}", header);
    }
    private void processTemplateContent() {
        commonBody = commonBody.replace("{{email}}", nameId);
        tableContent = attributes == null ? "No Attributes Received." : getTableContent();
        commonBody = commonBody.replace("{{tablecontent}}", tableContent);
        template = template.replace("{{commonbody}}", commonBody);
    }

    private void processExceptionTemplate() {
        exceptionBody = exceptionBody.replace("{{exceptionmessage}}", samlException.getMessage());
        template = template.replace("{{commonbody}}", exceptionBody);
    }

    private void processTemplateFooter() {
        template = template.replace("{{footer}}", footer);
    }

    private String getTableContent() {
        String tableContent = "";
        for (Map.Entry<String, String[]> m : attributes.entrySet()) {
            tableContent += this.tableContent.replace("{{key}}", m.getKey());
            tableContent = tableContent.replace("{{value}}", m.getValue()[0]);
        }
        return tableContent;
    }

    private String getImageURL(String imgName) throws IOException {
        String appName = this.properties.getProperty("appName");

        return "?resource=/resources/images/" + imgName;
    }
}
