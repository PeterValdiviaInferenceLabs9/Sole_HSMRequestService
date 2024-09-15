/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.beans;

/**
 *
 * @author Luis D
 */
public class TemplateButton {

    private String text;
    private String payload;
    private String buttonType;

    public TemplateButton() {
    }

    public TemplateButton(String text, String payload, String buttonType) {
        this.text = text;
        this.payload = payload;
        this.buttonType = buttonType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

}
