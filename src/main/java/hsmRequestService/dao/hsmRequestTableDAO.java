/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.dao;

import desktopframework.ConexionPoolObject;
import desktopframework.Configuracion;
import desktopframework.Fecha;
import desktopframework.Log;
import hsmRequestService.beans.HSMRequest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Luis D
 */
public class hsmRequestTableDAO {

    private static final String pool_name = Configuracion.getValueString("BD_RESOURCE");

    public static boolean insert(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "hsmRequestTableDAO.insert() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;
        ConexionPoolObject refConexionPoolObject = new ConexionPoolObject(pool_name);
        String hsmTable_name = refHSMRequest.getTemplate().trim();

        try {
            conn = refConexionPoolObject.getConnection();
            if (conn != null) {

                cstmt = conn.prepareCall("INSERT INTO " + hsmTable_name + "(agente,telefonoCliente,createdAt,messageContent)VALUES(?,?,?,?);");

                cstmt.setString(i++, refHSMRequest.getAgent_username());
                cstmt.setString(i++, refHSMRequest.getClient_phoneNumber());
                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, refHSMRequest.getRefHSMTemplate().getContent());

                Log.debug(LOG_HEADER + "Query : " + cstmt.toString(), SERVLET);
                Log.debug(LOG_HEADER + "IN : agente = " + refHSMRequest.getAgent_username(), SERVLET);
                Log.debug(LOG_HEADER + "IN : telefonoCliente = " + refHSMRequest.getClient_phoneNumber(), SERVLET);
                //Log.debug(LOG_HEADER + "IN : messageContent = " + refHSMRequest.getRefHSMTemplate().getContent(), SERVLET);

                cstmt.execute();
                exito = true;
                Log.debug(LOG_HEADER + "Inserción correcta", SERVLET);
            }
        } catch (SQLException ex) {
            Log.error(LOG_HEADER, ex, SERVLET);
        } finally {
            try {
                cstmt.close();
            } catch (SQLException ee) {
                Log.error(LOG_HEADER, ee, SERVLET);
            }
            refConexionPoolObject.disconnect();
        }
        return exito;
    }

    public static boolean createTable(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "hsmRequestTableDAO.createTable() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;
        ConexionPoolObject refConexionPoolObject = new ConexionPoolObject(pool_name);
        String hsmTable_name = refHSMRequest.getTemplate().trim();

        try {
            conn = refConexionPoolObject.getConnection();
            if (conn != null) {

                cstmt = conn.prepareCall("CREATE TABLE `" + hsmTable_name + "` (\n"
                        + "  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                        + "  `agente` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,\n"
                        + "  `telefonoCliente` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,\n"
                        + "  `createdAt` datetime NOT NULL,\n"
                        + "  `messageContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL\n"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");

                //Log.debug(LOG_HEADER + "Query : " + cstmt.toString(), SERVLET);
                Log.debug(LOG_HEADER + "IN : hsmTable_name = " + hsmTable_name, SERVLET);

                cstmt.execute();
                exito = true;
                Log.debug(LOG_HEADER + "Inserción correcta", SERVLET);
            }
        } catch (SQLException ex) {
            Log.error(LOG_HEADER, ex, SERVLET);
        } finally {
            try {
                cstmt.close();
            } catch (SQLException ee) {
                Log.error(LOG_HEADER, ee, SERVLET);
            }
            refConexionPoolObject.disconnect();
        }
        return exito;
    }

    public static String getLastAgentByAniAndTemplate(String hsmTable_name, String ani, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "hsmRequestTableDAO.getLastAgentByAniAndTemplate() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        String ref = null;
        ConexionPoolObject refConexionPoolObject = new ConexionPoolObject(pool_name);

        try {
            conn = refConexionPoolObject.getConnection();
            if (conn != null) {
                cstmt = conn.prepareCall("SELECT agente FROM " + hsmTable_name + " WHERE telefonoCliente=? ORDER BY createdAt DESC LIMIT 1;");
                cstmt.setString(i++, ani);
                rs = ConexionPoolObject.getResultSet(cstmt);
                if (rs != null) {
                    while (rs.next()) {
                        ref = rs.getString("agente");
                        Log.debug(LOG_HEADER + "OUT: ref = " + ref, SERVLET);
                    }
                }
            }
        } catch (SQLException ex) {
            Log.error(LOG_HEADER, ex, SERVLET);
        } finally {
            try {
                rs.close();
                cstmt.close();
            } catch (SQLException ee) {
                Log.error(LOG_HEADER, ee, SERVLET);
            }
            refConexionPoolObject.disconnect();
        }
        return ref;
    }
}
