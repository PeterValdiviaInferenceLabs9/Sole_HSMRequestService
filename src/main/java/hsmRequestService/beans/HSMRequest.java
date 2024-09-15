/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.beans;

import desktopframework.Log;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Luis D
 */
public class HSMRequest {

    private String agent_username;
    private String client_phoneNumber;
    private String template;
    private HSMTemplate refHSMTemplate;
    private ArrayList<String> fieldsRequestList = new ArrayList<String>();

    public HSMRequest() {
    }

    public HSMRequest(String agent_username, String client_phoneNumber, String template) {
        this.agent_username = agent_username;
        this.client_phoneNumber = client_phoneNumber;
        this.template = template;
    }

    public HSMRequest(HttpServletRequest refRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "HSMRequest() - ";
        this.client_phoneNumber = refRequest.getParameter("aniClient");
        this.template = refRequest.getParameter("templateName");
        this.agent_username = refRequest.getParameter("agentUser");

        Log.info(LOG_HEADER + "######### HSM REQUEST INPUT FIELDS - START ###########", SERVLET);
        Log.info(LOG_HEADER + "client_ani = " + this.client_phoneNumber, SERVLET);
        Log.info(LOG_HEADER + "template_name = " + this.template, SERVLET);
        Log.info(LOG_HEADER + "agent_username = " + this.agent_username, SERVLET);
        Log.info(LOG_HEADER + "######### HSM REQUEST INPUT FIELDS - END ###########", SERVLET);
    }

    public String getAgent_username() {
        return agent_username;
    }

    public void setAgent_username(String agent_username) {
        this.agent_username = agent_username;
    }

    public String getClient_phoneNumber() {
        return client_phoneNumber;
    }

    public void setClient_phoneNumber(String client_phoneNumber) {
        this.client_phoneNumber = client_phoneNumber;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public HSMTemplate getRefHSMTemplate() {
        return refHSMTemplate;
    }

    public void setRefHSMTemplate(HSMTemplate refHSMTemplate) {
        this.refHSMTemplate = refHSMTemplate;
    }

    public ArrayList<String> getFieldsRequestList() {
        return fieldsRequestList;
    }

    public boolean fillFieldsRequestList(HttpServletRequest request, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "HSMRequest.fillFieldsRequestList() - ";

        if (refHSMTemplate != null) {
            Log.info(LOG_HEADER + "######### HSM REQUEST TEMPLATE FIELDS FILL - START ###########", SERVLET);
            for (int w = 1; w <= this.refHSMTemplate.getNumFields(); w++) {
                String temp = request.getParameter("field" + w);
                this.fieldsRequestList.add(temp);
                Log.info(LOG_HEADER + "field" + w + " = " + temp, SERVLET);
            }
            Log.info(LOG_HEADER + "######### HSM REQUEST TEMPLATE FIELDS FILL - END ###########", SERVLET);
            return true;
        } else {
            return false;
        }
    }
}
