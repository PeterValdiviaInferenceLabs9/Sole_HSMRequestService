/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package hsmRequestService.servlets;

import desktopframework.Aplicacion;
import desktopframework.ConexionBDConfiguracion;
import desktopframework.Configuracion;
import desktopframework.Fecha;
import desktopframework.Log;
import hsmRequestService.beans.ClientHsmRegistry;
import hsmRequestService.dao.clientHsmRegistryTableDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luis D
 */
public class hsmCheckHsmRgstryDate extends HttpServlet {

    private final String SERVLET = "hsmCheckHsmRgstryDate";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String uuid = UUID.randomUUID().toString() + "-";
        final String SUBHEADER = ".processRequest() - ";
        final String LOG_HEADER = uuid + SERVLET + SUBHEADER;

        String request_telephone;
        Date lastUpdated_rs, actual_date;
        ClientHsmRegistry refClientHsmRegistry = null;
        String agent_username = "NULO";
        String diff_hour_lastUpdated = "NULO";
        int status = 400;

        Aplicacion.directorio = "C:\\home\\luisInference\\hsmRequestService";
        Configuracion.actualizar();
        ConexionBDConfiguracion.actualizar();
        Log.actualizar();

        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Inicio de Servicio *****************", SERVLET);
        Log.debug(LOG_HEADER + "ContextPath = " + request.getContextPath(), SERVLET);//Obtener contexto de URI
        Log.debug(LOG_HEADER + "Method = " + request.getMethod(), SERVLET);//Obtener metodo del request (Get o Post)
        Log.debug(LOG_HEADER + "QueryString = " + request.getQueryString(), SERVLET);
        Log.debug(LOG_HEADER + "PathInfo = " + request.getPathInfo(), SERVLET);

        request_telephone = request.getParameter("telephone");
        actual_date = new Date();

        Log.info(LOG_HEADER + "INPUT: request_telephone = " + request_telephone, SERVLET);

        refClientHsmRegistry = clientHsmRegistryTableDAO.getClientHsmRegistryByPhone(request_telephone, SERVLET, uuid);

        if (refClientHsmRegistry != null) {
            lastUpdated_rs = refClientHsmRegistry.getUpdatedAt();
            agent_username = refClientHsmRegistry.getAgent();
            diff_hour_lastUpdated = Long.toString(Fecha.calcularDiferenciaHoras(lastUpdated_rs, actual_date));
            Log.info(LOG_HEADER + "OUTPUT: diff_hour_lastUpdated = " + diff_hour_lastUpdated, SERVLET);
            Log.info(LOG_HEADER + "OUTPUT: agent_username = " + agent_username, SERVLET);
            status = 200;
        }

        String data = "{"
                + "\n"
                + "\"diferenciaEnHoras\": \"" + diff_hour_lastUpdated + "\",\n"
                + "\"agente\": \"" + agent_username + "\"\n"
                + "}";

        Log.info(LOG_HEADER + "OUTPUT: data = " + data, SERVLET);
        Log.info(LOG_HEADER + "OUTPUT: status = " + status, SERVLET);

        PrintWriter out = response.getWriter();
        response.setStatus(status);
        out.print(data);
        out.flush();

        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Fin de Servicio *****************", SERVLET);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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

}
