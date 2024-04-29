package org.example;

import java.util.List;

public class ClientInformation {
    private int id;
    private String ClientName;
    private List<String>  publicMessages;
    private List<String>  privateMessages;

    ClientInformation(String ClientName, List<String> publicMessages,int id) {
        this.ClientName = ClientName;
        this.publicMessages = publicMessages;
        this.id = id;
    }

    public void addMessage(String Message){
        publicMessages.add(Message);
    }
    public void addPrivateMessage(String Message){
        privateMessages.add(Message);
    }
    public String getClientName() {
        return ClientName;
    }
    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }
    public List<String> getPublicMessages() {
        return publicMessages;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
