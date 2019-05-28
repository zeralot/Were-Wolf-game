/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Character.CharacterInfo;
import java.net.Socket;

/**
 *
 * @author Be Khanh Duy
 */
public class ClientInfo {
    private Socket socket;
    private CharacterInfo userinfo;
    private int characterType;
    private int noVote;
    private boolean isStarted = false;
    private boolean awaken = false;
    private boolean dead = false;

    public Socket getSocket() {
        return socket;
    }

    public void setCharacterType(int characterType) {
        this.characterType = characterType;
        this.userinfo.setCharType(characterType);
    }

    public int getCharacterType() {
        return characterType;
    }
    
    

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public CharacterInfo getCharacterInfo() {
        return userinfo;
    }

    public void setUserinfo(CharacterInfo userinfo) {
        this.userinfo = userinfo;
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public boolean isIsStarted() {
        return isStarted;
    }

    public boolean isAwaken() {
        return awaken;
    }

    public void setAwaken(boolean awaken) {
        this.awaken = awaken;
    }

    public int getNoVote() {
        return noVote;
    }

    public void setNoVote(int noVote) {
        this.noVote = noVote;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDead() {
        return dead;
    }
    
    
}
