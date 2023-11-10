package org.NilsCorentin.server;

import org.NilsCorentin.client.Client;

import java.util.ArrayList;
import java.util.List;

public class PrivateChat {

    private ClientHandler sender;
    private ClientHandler receiver;
    private ArrayList<String> message;

    public PrivateChat(ClientHandler sender, ClientHandler receiver, ArrayList<String> message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }


    public List<ClientHandler> getParticipants() {
        return List.of(sender, receiver);
    }

    public ClientHandler getSender() {
        return sender;
    }

    public void setSender(ClientHandler sender) {
        this.sender = sender;
    }

    public ClientHandler getReceiver() {
        return receiver;
    }

    public void setReceiver(ClientHandler receiver) {
        this.receiver = receiver;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }
}
