/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Message;

import Character.CharacterInfo;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Be Khanh Duy
 */
public class ExchangeMessage implements Serializable{
    public static final int VILLAGE_TYPE = 0;
    public static final int WEREWOLF_TYPE = 1;
    public static final int SEER_TYPE = 2;
    public static final int PROTETOR_TYPE  = 3;
    public static final int WIZARD_TYPE = 4;
    
    public static final int SERVER_SEND_ONLINE_USERS = 0;
    public static final int CLIENT_SEND_INFO_TO_SERVER = 1;
    public static final int SERVER_NOTIFY_TO_CLIENT_NEWONLINE = 2;
    public static final int SERVER_NOTIFY_TO_CLIENT_OFFONLINE = 3;
    public static final int SERVER_NOTIFY_TO_CLIENT_NEWMESSAGE = 4;
    public static final int CLIENT_SEND_CHAT_MESSAGE = 5;
    public static final int CLIENT_SEND_READY_STATUS = 6;
    public static final int SERVER_NOTIFY_CHANGE_STATUS = 7;
    public static final int SERVER_NOTIFY_GAME_BEGIN  =8;
    public static final int SERVER_SEND_MESSAGE = 9;
    public static final int SERVER_NOTIFY_WEREWOLF = 10;
    public static final int WEREWOLF_SEND_VICTOM = 11;
    public static final int SERVER_NOTIFY_PROTECTOR = 12;
    public static final int SERVER_NOTIFY_SEER = 13;
    public static final int PROTECTOR_SEND_TARGET = 14;
    public static final int SEER_SEND_TARGET = 15;
    public static final int SERVER_SEND_CHECK_RESULT = 16;
    public static final int SERVER_SEND_VICTIM_TO_WIZARD = 17;
    public static final int WIZARD_SEND_CHOICE_TO_SERVER = 18;
    public static final int SERVER_NOTIFY_DAY_BEGIN = 19;
    public static final int SERVER_SEND_DEAD_LIST = 20;
    public static final int CLIENT_SEND_VOTE = 21;
    public static final int SERVER_SEND_VOTE_SUCCESS = 22;
    public static final int SERVER_NOTIFY_NIGHT_BEGIN = 23;
    public static final int SERVER_NOTIFY_WOLF_WIN = 25;
    public static final int SERVER_NOTIFY_HUMAN_WIN = 26;
    public static final int CLIENT_SEND_ALL_CHAT = 27;
    public static final int SERVER_NOTIFY_ALL_CHAT = 28;


    
    private CharacterInfo sender;
    private CharacterInfo receiver;
    private int command;
    private String content;
    private ArrayList<CharacterInfo> users = new ArrayList<>();
    private int charType;
    private CharacterInfo target;
    private boolean isProtected;

    public ArrayList<CharacterInfo> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<CharacterInfo> users) {
        this.users = users;
    }
    
    public CharacterInfo getSender() {
        return sender;
    }

    public void setSender(CharacterInfo sender) {
        this.sender = sender;
    }

    public CharacterInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(CharacterInfo receiver) {
        this.receiver = receiver;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setCharType(int charType){
        this.charType = charType;
    }

    public int getCharType() {
        return charType;
    }

    public void setTarget(CharacterInfo target) {
        this.target = target;
    }

    public CharacterInfo getTarget() {
        return target;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public boolean isIsProtected() {
        return isProtected;
    }
    
}
