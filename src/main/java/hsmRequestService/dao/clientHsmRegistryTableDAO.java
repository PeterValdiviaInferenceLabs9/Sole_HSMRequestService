/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.dao;

import desktopframework.ConexionPoolObject;
import desktopframework.Configuracion;
import desktopframework.Fecha;
import desktopframework.Log;
import hsmRequestService.beans.ClientHsmRegistry;
import hsmRequestService.beans.HSMRequest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Luis D
 */
public class clientHsmRegistryTableDAO {

    private static final String POOL_NAME = Configuracion.getValueString("BD_RESOURCE");
    private static final String TABLE_NAME = "client_hsm_registry";

    public static boolean insert(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "clientHsmRegistryTableDAO.insert() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;
        ConexionPoolObject refConexionPoolObject = new ConexionPoolObject(POOL_NAME);

        try {
            conn = refConexionPoolObject.getConnection();
            if (conn != null) {

                cstmt = conn.prepareCall("INSERT INTO " + TABLE_NAME + "(telefonoCliente,createdAt,updatedAt,lastHsmSent,agent)VALUES(?,?,?,?,?);");

                cstmt.setString(i++, refHSMRequest.getClient_phoneNumber());
                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, refHSMRequest.getTemplate().trim());
                cstmt.setString(i++, refHSMRequest.getAgent_username());

                Log.debug(LOG_HEADER + "Query : " + cstmt.toString(), SERVLET);
                Log.debug(LOG_HEADER + "IN : telefonoCliente = " + refHSMRequest.getClient_phoneNumber(), SERVLET);
                Log.debug(LOG_HEADER + "IN : lastHsmSent = " + refHSMRequest.getTemplate().trim(), SERVLET);
                Log.debug(LOG_HEADER + "IN : lastHsmSent = " + refHSMRequest.getAgent_username(), SERVLET);

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

    public static ClientHsmRegistry getClientHsmRegistryByPhone(String telephone, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "clientHsmRegistryTableDAO.getClientHsmRegistryByPhone() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        ClientHsmRegistry refClientHsmRegistry = null;
        ConexionPoolObject refConexionPoolObject = new ConexionPoolObject(POOL_NAME);

        try {
            conn = refConexionPoolObject.getConnection();
            if (conn != null) {

                cstmt = conn.prepareCall("SELECT telefonoCliente,createdAt,updatedAt,lastHsmSent,agent FROM " + TABLE_NAME + " WHERE telefonoCliente=?;");

                cstmt.setString(i++, telephone);
                rs = ConexionPoolObject.getResultSet(cstmt);

                if (rs != null) {
                    while (rs.next()) {
                        refClientHsmRegistry = new ClientHsmRegistry();
                        refClientHsmRegistry.setTelefonoCliente(rs.getString("telefonoCliente"));
                        refClientHsmRegistry.setCreatedAt(Fecha.obtenerDateDeUnStringConHora(rs.getString("createdAt")));
                        refClientHsmRegistry.setUpdatedAt(Fecha.obtenerDateDeUnStringConHora(rs.getString("updatedAt")));
                        refClientHsmRegistry.setLastHsmSent(rs.getString("lastHsmSent"));
                        refClientHsmRegistry.setAgent(rs.getString("agent"));

                        Log.debug(LOG_HEADER + "OUT: telefonoCliente = " + refClientHsmRegistry.getTelefonoCliente(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: createdAt = " + refClientHsmRegistry.getCreatedAt(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: updatedAt = " + refClientHsmRegistry.getUpdatedAt(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: lastHsmSent = " + refClientHsmRegistry.getLastHsmSent(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: agent = " + refClientHsmRegistry.getAgent(), SERVLET);
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
        return refClientHsmRegistry;
    }

    public static boolean update(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "clientHsmRegistryTableDAO.update() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;
        ConexionPoolObject refConexionPoolObject = new ConexionPoolObject(POOL_NAME);
        try {
            conn = refConexionPoolObject.getConnection();
            if (conn != null) {
                cstmt = conn.prepareCall("UPDATE " + TABLE_NAME + " SET updatedAt=?, lastHsmSent=?, agent=? WHERE telefonoCliente=?;");

                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, refHSMRequest.getTemplate().trim());
                cstmt.setString(i++, refHSMRequest.getAgent_username());
                cstmt.setString(i++, refHSMRequest.getClient_phoneNumber());

                Log.debug(LOG_HEADER + "Query : " + cstmt.toString());
                Log.debug(LOG_HEADER + "IN : lastHsmSent = " + refHSMRequest.getTemplate().trim(), SERVLET);
                Log.debug(LOG_HEADER + "IN : agent = " + refHSMRequest.getAgent_username(), SERVLET);
                Log.debug(LOG_HEADER + "IN : telefonoCliente = " + refHSMRequest.getClient_phoneNumber(), SERVLET);

                cstmt.execute();
                exito = true;
                Log.debug(LOG_HEADER + "Actualización correcta", SERVLET);
            }
        } catch (NullPointerException | SQLException ex) {
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
}
