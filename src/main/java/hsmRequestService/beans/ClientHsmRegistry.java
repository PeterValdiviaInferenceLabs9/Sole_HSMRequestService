/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.beans;

import java.util.Date;

/**
 *
 * @author Luis D
 */
public class ClientHsmRegistry {
    private String telefonoCliente;
    private Date createdAt;
    private Date updatedAt;
    private String lastHsmSent;
    private String agent;

    public ClientHsmRegistry() {
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastHsmSent() {
        return lastHsmSent;
    }

    public void setLastHsmSent(String lastHsmSent) {
        this.lastHsmSent = lastHsmSent;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
