/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package hsmRequestService.servlets;

import desktopframework.Aplicacion;
import desktopframework.ConexionBDConfiguracion;
import desktopframework.Configuracion;
import desktopframework.Log;
import desktopframework.web.Header;
import desktopframework.web.ServicioWeb;
import hsmRequestService.beans.HSMApiResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import hsmRequestService.beans.HSMRequest;
import hsmRequestService.beans.HSMTemplate;
import hsmRequestService.beans.TemplateButton;
import hsmRequestService.controllers.clientHsmRegistryController;
import hsmRequestService.dao.hsmRequestTableDAO;
import hsmRequestService.tools.StringTools;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Luis D
 */
public class SolicitudHSMSole extends HttpServlet {

    private final String SERVLET = "SolicitudHSMSole";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String uuid = UUID.randomUUID().toString() + "-";
        final String SUBHEADER = ".processRequest() - ";
        final String LOG_HEADER = uuid + SERVLET + SUBHEADER;
        ArrayList<String> fieldsRequest = new ArrayList<String>();
        String responseToSend = null;

        //Config params
        Aplicacion.directorio = "C:\\home\\luisInference\\hsmRequestService";
        Configuracion.actualizar();
        ConexionBDConfiguracion.actualizar();
        Log.actualizar();

        //Service params
        int status = 200;
        String body_rs = "OK";

        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Inicio de Servicio *****************", SERVLET);
        Log.debug(LOG_HEADER + "ContextPath = " + request.getContextPath(), SERVLET);//Obtener contexto de URI
        Log.debug(LOG_HEADER + "Method = " + request.getMethod(), SERVLET);//Obtener metodo del request (Get o Post)
        Log.debug(LOG_HEADER + "QueryString = " + request.getQueryString(), SERVLET);
        Log.debug(LOG_HEADER + "PathInfo = " + request.getPathInfo(), SERVLET);

        HSMRequest refSolicitudHSM = new HSMRequest(request, SERVLET, uuid);//Getting HSMRequest parameters
        HSMTemplate refHSMTemplate = lookTemplateOnDomainNewEdna(refSolicitudHSM.getTemplate(), SERVLET, uuid);//Looking for template on Edna Sole Domain

        refSolicitudHSM.setRefHSMTemplate(refHSMTemplate);

        if (refSolicitudHSM.getRefHSMTemplate() != null) {
            refSolicitudHSM.fillFieldsRequestList(request, SERVLET, uuid);//Checking number of fields for template in request and start fill up process        
            HSMApiResponse refApiResponse = sendImOutHSMAPINewEdna(refSolicitudHSM, SERVLET, uuid);

            if (refApiResponse.getStatus() == 200) {
                //Request was send by API sucessfully
                clientHsmRegistryController.registrarHsmRequest(refSolicitudHSM, SERVLET, uuid);//Inserting successful request in table
                clientHsmRegistryController.actualizarRegistroCliente(refSolicitudHSM, SERVLET, uuid);//Updating/Creating registry on table client_hsm_registry
                response.sendRedirect("https://web-prd.inferencelabs9.com/HSMRequestService-1.0/soleHSMResultado.jsp?template=" + refSolicitudHSM.getTemplate() + "&agent=" + refSolicitudHSM.getAgent_username() + "&ani=" + refSolicitudHSM.getClient_phoneNumber() + "&rs=" + body_rs);
            } else {
                //Request could not be sent by API, something failed
                body_rs = refApiResponse.getBody();
                response.sendRedirect("https://web-prd.inferencelabs9.com/HSMRequestService-1.0/soleHSMResultado.jsp?template=" + refSolicitudHSM.getTemplate() + "&agent=" + refSolicitudHSM.getAgent_username() + "&ani=" + refSolicitudHSM.getClient_phoneNumber() + "&rs=" + body_rs);
            }

        } else {
            body_rs = "La plantilla seleccionada no es válida o no esta registrada para el tenantId utilizado!";
            Log.info(LOG_HEADER + "El template solicitado no existe o no esta registrado en la tabla!", SERVLET);
            response.sendRedirect("https://web-prd.inferencelabs9.com/HSMRequestService-1.0/soleHSMResultado.jsp?template=" + refSolicitudHSM.getTemplate() + "&agent=" + refSolicitudHSM.getAgent_username() + "&ani=" + refSolicitudHSM.getClient_phoneNumber() + "&rs=" + body_rs);
        }

        PrintWriter out = response.getWriter();
        response.setStatus(status);
        out.print(body_rs);
        out.flush();
        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Fin de Servicio *****************", SERVLET);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public HSMTemplate lookTemplateOnDomain(String template_name, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + SERVLET + ".lookTemplateOnDomain() - ";
        String xApiKeyWS_request = Configuracion.getValueString("API_KEY_WHATSAPP");
        HSMTemplate tempHSMTemplate = null;

        JSONObject refJSONObject;
        int statusCode = -1;
        String response_body = null;

        String url = "https://im.edna.ru/api/getOutMessageMatchers";
        //String url = "https://app.edna.io/api/message-matchers/get-by-request";

        List<Header> listaHeaders = new ArrayList();
        HttpResponse<String> refHttpResponse;
        listaHeaders.add(new Header("X-API-KEY", xApiKeyWS_request));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        String jsonQuery = "{\n"
                + "\"imType\": \"whatsapp\"\n"
                + "}";

        refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, jsonQuery);

        statusCode = refHttpResponse.statusCode();
        response_body = refHttpResponse.body();
        Log.info(LOG_HEADER + "OUTPUT: statusCode = " + statusCode, SERVLET);
        //Log.info(LOG_HEADER + "OUTPUT: body = " + response_body, SERVLET);

        try {
            refJSONObject = new JSONObject(response_body);//JSONObject with list of HSM templates approved by Meta
            JSONArray arrayRs = refJSONObject.getJSONArray("result");
            int rsLen = arrayRs.length();

            //Loop refJSONObject until find template_name
            for (int i = 0; i < rsLen; i++) {
                JSONObject temp = arrayRs.getJSONObject(i);
                String tempName = temp.getString("name").trim();

                if (tempName.equals(template_name)) {
                    Log.info(LOG_HEADER + "############## TEMPLATE INFO - START ##############", SERVLET);
                    tempHSMTemplate = new HSMTemplate();
                    tempHSMTemplate.setName(tempName);//Save template name
                    Log.info(LOG_HEADER + "Template name: " + tempHSMTemplate.getName(), SERVLET);

                    JSONObject contentObject = temp.getJSONObject("content");
                    JSONObject headerObject = contentObject.getJSONObject("header");
                    String tempTemplateText = contentObject.getString("text");
                    tempHSMTemplate.setContent(tempTemplateText);//Save text of template
                    Log.info(LOG_HEADER + "Content: " + tempHSMTemplate.getContent(), SERVLET);

                    JSONObject keyboardObject = contentObject.getJSONObject("keyboard");
                    JSONObject rowObject = keyboardObject.getJSONObject("row");
                    JSONArray buttonsArray = rowObject.getJSONArray("buttons");

                    int buttonsArrayLen = buttonsArray.length();

                    for (int x = 0; x < buttonsArrayLen; x++) {
                        JSONObject buttonObject = buttonsArray.getJSONObject(x);
                        String tempButtonText = buttonObject.getString("text");
                        String tempButtonPayload = buttonObject.getString("payload");
                        String tempButtonType = buttonObject.getString("buttonType");

                        tempHSMTemplate.addButton(new TemplateButton(tempButtonText, tempButtonPayload, tempButtonType));
                        Log.info(LOG_HEADER + "Button " + (x + 1) + " - text: " + tempHSMTemplate.getRefTemplateButtonList().get(x).getText(), SERVLET);
                        Log.info(LOG_HEADER + "Button " + (x + 1) + " - payload: " + tempHSMTemplate.getRefTemplateButtonList().get(x).getPayload(), SERVLET);
                        Log.info(LOG_HEADER + "Button " + (x + 1) + " - buttonType: " + tempHSMTemplate.getRefTemplateButtonList().get(x).getButtonType(), SERVLET);
                    }

                    tempHSMTemplate.setNumFields(StringTools.getNumberOfRepeatedWords("\\{\\{\\d\\}\\}", tempHSMTemplate.getContent()));
                    Log.info(LOG_HEADER + "numFields: " + tempHSMTemplate.getNumFields(), SERVLET);
                    Log.info(LOG_HEADER + "############## TEMPLATE INFO - END ##############", SERVLET);
                    break;
                }
            }

        } catch (JSONException ee) {
            Log.info(LOG_HEADER + ee, SERVLET);
        }

        return tempHSMTemplate;
    }

    public HSMTemplate lookTemplateOnDomainNewEdna(String template_name, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + SERVLET + ".lookTemplateOnDomainNewEdna() - ";
        String xApiKeyWS_request = Configuracion.getValueString("API_KEY_WHATSAPP");
        String subject_id = Configuracion.getValueString("SUBJECT_WHATSAPP_ID");
        HSMTemplate tempHSMTemplate = null;

        JSONArray refJSONArray;
        int statusCode = -1;
        String response_body = null;

        String url = "https://app.edna.io/api/message-matchers/get-by-request";

        List<Header> listaHeaders = new ArrayList();
        HttpResponse<String> refHttpResponse;
        listaHeaders.add(new Header("X-API-KEY", xApiKeyWS_request));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        String jsonQuery = "{\n"
                + "\"subjectId\": " + subject_id + ",\n"
                + "\"matcherTypes\": \"USER\"\n"
                + "}";

        refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, jsonQuery);

        statusCode = refHttpResponse.statusCode();
        response_body = refHttpResponse.body();
        Log.info(LOG_HEADER + "OUTPUT: statusCode = " + statusCode, SERVLET);
        //Log.info(LOG_HEADER + "OUTPUT: body = " + response_body, SERVLET);

        try {
            refJSONArray = new JSONArray(response_body);//JSONObject with list of HSM templates approved by Meta
            int rsLen = refJSONArray.length();

            //Loop refJSONObject until find template_name
            for (int i = 0; i < rsLen; i++) {
                JSONObject temp = refJSONArray.getJSONObject(i);
                String tempName = temp.getString("name").trim();

                if (tempName.equals(template_name)) {
                    Log.info(LOG_HEADER + "############## TEMPLATE INFO - START ##############", SERVLET);
                    tempHSMTemplate = new HSMTemplate();
                    tempHSMTemplate.setName(tempName);//Save template name
                    Log.info(LOG_HEADER + "Template name: " + tempHSMTemplate.getName(), SERVLET);

                    JSONObject contentObject = temp.getJSONObject("content");
                    JSONObject headerObject = contentObject.optJSONObject("header");
                    String tempTemplateText = contentObject.getString("text");
                    tempHSMTemplate.setContent(tempTemplateText);//Save text of template
                    Log.info(LOG_HEADER + "Content: " + tempHSMTemplate.getContent(), SERVLET);

                    JSONObject keyboardObject = contentObject.getJSONObject("keyboard");
                    JSONArray rowsArray = keyboardObject.getJSONArray("rows");

                    if (rowsArray.length() > 0) {
                        JSONObject beforeButtons = rowsArray.getJSONObject(0);
                        JSONArray buttonsArray = beforeButtons.getJSONArray("buttons");

                        int buttonsArrayLen = buttonsArray.length();

                        for (int x = 0; x < buttonsArrayLen; x++) {
                            JSONObject buttonObject = buttonsArray.getJSONObject(x);
                            String tempButtonText = buttonObject.getString("text");
                            String tempButtonPayload = buttonObject.getString("payload");
                            String tempButtonType = buttonObject.getString("buttonType");

                            tempHSMTemplate.addButton(new TemplateButton(tempButtonText, tempButtonPayload, tempButtonType));
                            Log.info(LOG_HEADER + "Button " + (x + 1) + " - text: " + tempHSMTemplate.getRefTemplateButtonList().get(x).getText(), SERVLET);
                            Log.info(LOG_HEADER + "Button " + (x + 1) + " - payload: " + tempHSMTemplate.getRefTemplateButtonList().get(x).getPayload(), SERVLET);
                            Log.info(LOG_HEADER + "Button " + (x + 1) + " - buttonType: " + tempHSMTemplate.getRefTemplateButtonList().get(x).getButtonType(), SERVLET);
                        }
                    }

                    tempHSMTemplate.setNumFields(StringTools.getNumberOfRepeatedWords("\\{\\{(.*?)\\}\\}", tempHSMTemplate.getContent()));
                    Log.info(LOG_HEADER + "numFields: " + tempHSMTemplate.getNumFields(), SERVLET);
                    Log.info(LOG_HEADER + "############## TEMPLATE INFO - END ##############", SERVLET);
                    break;
                }
            }

        } catch (JSONException ee) {
            Log.info(LOG_HEADER + ee, SERVLET);
        }
        return tempHSMTemplate;
    }

    public boolean sendImOutHSMAPI(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + SERVLET + ".sendImOutHSMAPI() - ";
        String xApiKeyWS_request = Configuracion.getValueString("API_KEY_WHATSAPP");
        //*********************************API HSM Request Start*************************************//
        String url = "https://im.edna.io/api/imOutHSM";
        List<Header> listaHeaders = new ArrayList();
        HttpResponse<String> refHttpResponse;
        int statusCode = -1;

        listaHeaders.add(new Header("X-API-KEY", xApiKeyWS_request));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        String data = buildApiRequestHSM(refHSMRequest);

        Log.info(LOG_HEADER + "INPUT: url = " + url, SERVLET);
        Log.info(LOG_HEADER + "INPUT: data = " + data, SERVLET);

        refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, data);
        statusCode = refHttpResponse.statusCode();
        Log.info(LOG_HEADER + "OUTPUT: statusCode = " + statusCode, SERVLET);
        Log.info(LOG_HEADER + "OUTPUT: body = " + refHttpResponse.body(), SERVLET);

        if (statusCode == 200) {
            return true;
        } else {
            return false;
        }
        //*********************************API HSM Request End*************************************//
    }

    public HSMApiResponse sendImOutHSMAPINewEdna(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + SERVLET + ".sendImOutHSMAPINewEdna() - ";
        String xApiKeyWS_request = Configuracion.getValueString("API_KEY_WHATSAPP");
        //*********************************API HSM Request Start*************************************//
        String url = "https://app.edna.io/api/cascade/schedule";
        List<Header> listaHeaders = new ArrayList();
        HttpResponse<String> refHttpResponse;
        int statusCode = -1;

        HSMApiResponse refApiResponse = new HSMApiResponse();

        listaHeaders.add(new Header("X-API-KEY", xApiKeyWS_request));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        String data = buildApiRequestHSM(refHSMRequest);

        Log.info(LOG_HEADER + "INPUT: url = " + url, SERVLET);
        Log.info(LOG_HEADER + "INPUT: data = " + data, SERVLET);

        refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, data);
        statusCode = refHttpResponse.statusCode();
        Log.info(LOG_HEADER + "OUTPUT: statusCode = " + statusCode, SERVLET);
        Log.info(LOG_HEADER + "OUTPUT: body = " + refHttpResponse.body(), SERVLET);

        refApiResponse.setStatus(statusCode);
        try {
            JSONObject refJSONObject = new JSONObject(refHttpResponse.body());
            String temp_str = refJSONObject.optString("detail");

            if (temp_str.contains("insufficient funds")) {
                refApiResponse.setBody("Insufficient funds");
            } else if (temp_str.contains("Cascade template not found")) {
                refApiResponse.setBody("Cascade template not found");
            } else {
                refApiResponse.setBody(refJSONObject.optString("title"));
            }
        } catch (JSONException | NullPointerException ee) {
            Log.info(LOG_HEADER + ee, SERVLET);
            refApiResponse.setBody("Not manageable");
        }

        return refApiResponse;
        //*********************************API HSM Request End*************************************//
    }

    public String buildApiRequestHSM(HSMRequest refHSMRequest) {
        String buildHSMrs = " ";
        int fieldsQty = refHSMRequest.getRefHSMTemplate().getNumFields();
        String subject_request = Configuracion.getValueString("SUBJECT_WHATSAPP");
        String cascade_id = Configuracion.getValueString("CASCADE_WHATSAPP");
        String templateText = refHSMRequest.getRefHSMTemplate().getContent();

        templateText = formatTextTemplate(templateText);
        for (int y = 0; y < refHSMRequest.getFieldsRequestList().size(); y++) {
            templateText = templateText.replace("[[" + (y + 1) + "]]", refHSMRequest.getFieldsRequestList().get(y));
        }

        refHSMRequest.getRefHSMTemplate().setContent(templateText);//Updating content of Template with filled fields

        templateText = templateText.replace("\n", "\\n");
        templateText = templateText.replace("\r", "\\r");
        templateText = templateText.replace("\t", "\\t");

        buildHSMrs = "{\n"
                + "    \"requestId\": \"" + UUID.randomUUID() + "\",\n"
                + "    \"cascadeId\": \"" + cascade_id + "\",\n"
                + "    \"subscriberFilter\": {\n"
                + "         \"address\": \"" + refHSMRequest.getClient_phoneNumber() + "\",\n"
                + "         \"type\": \"PHONE\"\n"
                + "     },\n"
                //+ "    \"startTime\": \"" + Fecha.obtenerActualString("yyyy-MM-dd'T'HH\\:mm\\:ss.SSS'Z'") + "\",\n"
                + "    \"content\": {\n"
                + "         \"whatsappContent\": {\n"
                + "             \"contentType\": \"TEXT\",\n"
                + "             \"text\": \"" + templateText + "\",\n"
                + "             \"keyboard\": {\n"
                + "                 \"rows\": [";

        if (refHSMRequest.getRefHSMTemplate().getRefTemplateButtonList().size() > 0) {
            buildHSMrs += "\n{\n"
                    + "                     \"buttons\": [\n";

            for (int x = 0; x < refHSMRequest.getRefHSMTemplate().getRefTemplateButtonList().size(); x++) {

                if (x != 0) {
                    buildHSMrs += ",\n";
                }

                buildHSMrs += "{\n"
                        + "  \"text\": \"" + refHSMRequest.getRefHSMTemplate().getRefTemplateButtonList().get(x).getText() + "\",\n"
                        + "  \"buttonType\": \"" + refHSMRequest.getRefHSMTemplate().getRefTemplateButtonList().get(x).getButtonType() + "\",\n"
                        + "  \"payload\": \"" + refHSMRequest.getRefHSMTemplate().getRefTemplateButtonList().get(x).getPayload() + "\"\n"
                        + "}";
            }

            buildHSMrs += "                     ]"
                    + "                     }\n"
                    + "                 ]\n"
                    + "             }\n"
                    + "         }\n"
                    + "     }\n"
                    + " }";
        } else {
            buildHSMrs += "]\n"
                    + "             }\n"
                    + "         }\n"
                    + "     }\n"
                    + " }";
        }

        return buildHSMrs;
    }

    public String formatTextTemplate(String TemplateText) {
        String temp = TemplateText;
        int inicio, fin;
        int i = 1;

        do {
            inicio = temp.indexOf("{{");
            fin = temp.indexOf("}}") + 2;

            if (inicio >= 0) {
                temp = temp.replace(temp.substring(inicio, fin), "[[" + i + "]]");
                i++;
            }

        } while (inicio >= 0);
        return temp;
    }
}
