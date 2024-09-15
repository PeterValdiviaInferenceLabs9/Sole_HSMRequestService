/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.controllers;

import desktopframework.Log;
import hsmRequestService.beans.ClientHsmRegistry;
import hsmRequestService.beans.HSMRequest;
import hsmRequestService.dao.clientHsmRegistryTableDAO;
import hsmRequestService.dao.hsmRequestTableDAO;
import java.util.Date;

/**
 *
 * @author Luis D
 */
public class clientHsmRegistryController {

    public static void actualizarRegistroCliente(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "clientHsmRegistryController.actualizarRegistroCliente() - ";
        ClientHsmRegistry refLastUpdated;

        refLastUpdated = clientHsmRegistryTableDAO.getClientHsmRegistryByPhone(refHSMRequest.getClient_phoneNumber(), SERVLET, uuid);
        if (refLastUpdated == null) {
            clientHsmRegistryTableDAO.insert(refHSMRequest, SERVLET, uuid);//Registry is entered if its not created yet.
        } else {
            clientHsmRegistryTableDAO.update(refHSMRequest, SERVLET, uuid);//Registry is updated if its already created.
        }
    }

    public static void registrarHsmRequest(HSMRequest refHSMRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "clientHsmRegistryController.registrarHsmRequest() - ";
        boolean insert_rs = false;

        insert_rs = hsmRequestTableDAO.insert(refHSMRequest, SERVLET, uuid);//Inserting successful request in table

        if (insert_rs == false) {
            Log.info(LOG_HEADER + "Se procedera a crear la tabla con nombre " + refHSMRequest.getTemplate(), SERVLET);
            hsmRequestTableDAO.createTable(refHSMRequest, SERVLET, uuid);
            hsmRequestTableDAO.insert(refHSMRequest, SERVLET, uuid);//Inserting successful request in table
        }
    }
}
