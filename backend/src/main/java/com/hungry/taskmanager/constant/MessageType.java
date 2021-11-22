package com.hungry.taskmanager.constant;

public enum MessageType {
    G_INVITATION(0),
    F_INVITATION(1),
    G_APPLICATION(2);

    private final int key;

    private MessageType(int key){
        this.key = key;
    }

    public int getKey(){
        return this.key;
    }

}
