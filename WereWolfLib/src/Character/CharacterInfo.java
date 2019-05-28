/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Character;

import java.io.Serializable;

/**
 *
 * @author Be Khanh Duy
 */
public class  CharacterInfo implements Serializable{
    
    int charType;
    String userName;
    boolean ready;
    int noVote;
    
    public static final int VILLAGE = 0;
    public static final int WEREWOLF = 1;
    public static final int SEER = 2;
    public static final int PROTECTOR = 3;
    public static final int WIZARD = 4;

    public CharacterInfo() {
        this.ready = false;
        this.charType=-1;
        this.noVote=0;
    }
    public CharacterInfo(String userName){
        this.userName = userName;
        this.ready = false;
        this.charType=-1;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setCharType(int charType) {
        this.charType = charType;
    }

    public int getCharType() {
        return charType;
    }
    
    

    public String getUsername() {
        return userName;
    }

    public int getNoVote() {
        return noVote;
    }
    public void addVote(){
        this.noVote = this.noVote+1;
    }
    
    
    public void nightWake(){
        
    }
    
    public void dayWake(){
        
    }

    @Override
    public String toString() {
        if(this.ready == true){
        return userName + " ready";
    }
        else return userName; //To change body of generated methods, choose Tools | Templates.
    }
    
}
