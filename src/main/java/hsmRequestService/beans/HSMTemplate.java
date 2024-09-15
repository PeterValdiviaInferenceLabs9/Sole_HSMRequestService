/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hsmRequestService.beans;

import java.util.ArrayList;

/**
 *
 * @author Luis D
 */
public class HSMTemplate {
    
    private String name;
    private int numFields;
    private String content;
    private ArrayList<TemplateButton> refTemplateButtonList = new ArrayList<TemplateButton>();
    
    public HSMTemplate() {
    }
    
    public HSMTemplate(String name, int numFields, String content) {
        this.name = name;
        this.numFields = numFields;
        this.content = content;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getNumFields() {
        return numFields;
    }
    
    public void setNumFields(int numFields) {
        this.numFields = numFields;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<TemplateButton> getRefTemplateButtonList() {
        return refTemplateButtonList;
    }
    
    public void addButton(TemplateButton refTemplateButton) {
        this.refTemplateButtonList.add(refTemplateButton);
    }
    
    public int getNumberButtons(){
        return refTemplateButtonList.size();
    }
    
}
