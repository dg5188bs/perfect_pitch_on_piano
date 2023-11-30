package com.example.perfect_pitch_on_piano;

public class DBText {

    private String userText;
    private String id;

    public DBText(String userText, String id){
        this.userText = userText;
        this.id = id;
    }

    public String getUserText(){
        return userText;
    }

    public String getId() {
        return id;
    }
}
