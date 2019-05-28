/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Character.CharacterInfo;
import Message.ExchangeMessage;
import entity.ClientInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Be Khanh Duy
 */
public class Server {

    ServerSocket server;
    ArrayList<ClientInfo> clients = new ArrayList<>();
    ArrayList<CharacterInfo> voteList = new ArrayList<>();
    int total = 0;
    int ready = 0;
    int alive = 0;
    int maxVote = 0;
    int voted = 0;
    CharacterInfo hanger = null;
    boolean nightOver = false;
    boolean gameOver = false;
    boolean morningOver = false;
    CharacterInfo victim = null;
    CharacterInfo check = null;
    CharacterInfo protect = null;
    CharacterInfo unlucky = null;

    boolean wolfDone;
    boolean seerDone;
    boolean wizardDone;
    boolean protectorDone;

    boolean isProtected;

    public Server() {
        try {
            server = new ServerSocket(9999);
            log("Server Started.");
            ConnectionAcceptor acceptor = new ConnectionAcceptor();
            acceptor.start();
            log("Server stated accepting connection....");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessageToAll(String content) {
        ExchangeMessage message = new ExchangeMessage();
        for (ClientInfo client : clients) {
            OutputStream os_client = null;
            try {
                os_client = client.getSocket().getOutputStream();
                ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                message.setContent(content);
                message.setCommand(ExchangeMessage.SERVER_SEND_MESSAGE);
                oos_client.writeObject(message);
                log("Hello "
                        + " to " + client.getCharacterInfo().getUsername());
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void randomization() {
        ArrayList<Integer> a = new ArrayList<>();
        Random randm = new Random();
        int i = 0;
        int wolves = 0, seer = 0, protector = 0, wizard = 0, villages = 0;
        int noOfWolves = total / 3;
        boolean flag;
        while (i < total) {
            int tmp = randm.nextInt(total);
            flag = false;
            for (int j = 0; j < i; j++) {
                if (a.get(j) == tmp) {
                    flag = true;
                }
            }
            if (flag == false) {
                a.add(tmp);
//                    System.out.println(a.get(i));
                if (wolves < noOfWolves) {
                    clients.get(tmp).setCharacterType(ExchangeMessage.WEREWOLF_TYPE);
                    wolves++;
                } else if (seer < 1) {
                    clients.get(tmp).setCharacterType(ExchangeMessage.SEER_TYPE);
                    seer++;

                } else if (protector < 1) {
                    clients.get(tmp).setCharacterType(ExchangeMessage.PROTETOR_TYPE);
                    protector++;

                } else if (wizard < 1) {
                    clients.get(tmp).setCharacterType(ExchangeMessage.WIZARD_TYPE);
                    wizard++;

                } else {
                    clients.get(tmp).setCharacterType(ExchangeMessage.VILLAGE_TYPE);

                }

                i++;
            }
        }

    }

    class ConnectionAcceptor extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = server.accept();
                    ClientInfo client = new ClientInfo();
                    client.setSocket(socket);
                    clients.add(client);
                    log("client " + socket.getInetAddress().getHostAddress().toString()
                            + " has connected to Server.");
                    ClientHandler clienthandler = new ClientHandler(client);
                    clienthandler.start();
                    total++;
                    System.out.println(total);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    class ClientHandler extends Thread {

        boolean isRunning = true;
        ClientInfo current;

        public ClientHandler(ClientInfo client) {
            this.current = client;
        }

        public ArrayList<CharacterInfo> getOnlineUsers() {
            ArrayList<CharacterInfo> users = new ArrayList<>();
            for (ClientInfo client : clients) {
                users.add(client.getCharacterInfo());
            }
            return users;
        }

        @Override
        public void run() {
            try {
                OutputStream os = current.getSocket().getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                ExchangeMessage message = new ExchangeMessage();
                while (current.isIsStarted() == false) {

                    InputStream is = current.getSocket().getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    message = (ExchangeMessage) ois.readObject();
                    switch (message.getCommand()) {
                        case ExchangeMessage.CLIENT_SEND_INFO_TO_SERVER:
                            current.setUserinfo(message.getSender());
                            log("received infor of " + current.getCharacterInfo().getUsername());
                            message.setCommand(ExchangeMessage.SERVER_SEND_ONLINE_USERS);
                            message.setUsers(getOnlineUsers());
                            oos.writeObject(message);

                            message.setCommand(ExchangeMessage.SERVER_NOTIFY_TO_CLIENT_NEWONLINE);
                            message.setUsers(getOnlineUsers());
                            for (ClientInfo client : clients) {
                                OutputStream os_client = client.getSocket().getOutputStream();
                                ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                oos_client.writeObject(message);
                                log("Notify infor of " + current.getCharacterInfo().getUsername()
                                        + " to " + client.getCharacterInfo().getUsername()
                                );
                            }
                            break;
                        case ExchangeMessage.CLIENT_SEND_CHAT_MESSAGE:
                            message.setCommand(ExchangeMessage.SERVER_NOTIFY_TO_CLIENT_NEWMESSAGE);
                            log("Sender: " + message.getSender() + " SEND TO " + message.getReceiver()
                                    + " content: " + message.getContent());
                            Socket receiverSocket = getReceiverConnection(message.getReceiver());
                            OutputStream receiver_os = receiverSocket.getOutputStream();
                            ObjectOutputStream receiver_oos = new ObjectOutputStream(receiver_os);
                            receiver_oos.writeObject(message);
                            break;
                        case ExchangeMessage.CLIENT_SEND_READY_STATUS:
                            message.setCommand(ExchangeMessage.SERVER_NOTIFY_CHANGE_STATUS);
                            log("Client: " + message.getSender() + "has changed to ready.");
                            for (ClientInfo client : clients) {
                                OutputStream os_client = client.getSocket().getOutputStream();
                                ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                oos_client.writeObject(message);
                                log("Notify infor of " + current.getCharacterInfo().getUsername()
                                        + " to " + client.getCharacterInfo().getUsername()
                                );

                            }
                            ready++;
                            System.out.println();
                            if (total >= 2 && total == ready && current.isIsStarted() == false) {
                                current.setIsStarted(true);
                                alive = total;
                                for (ClientInfo client : clients) {
                                    client.setIsStarted(true);
                                }
                                randomization();
                                message.setCommand(ExchangeMessage.SERVER_NOTIFY_GAME_BEGIN);
                                log("Game Start");
                                current.setIsStarted(true);
                                for (ClientInfo client : clients) {
                                    OutputStream os_client = client.getSocket().getOutputStream();
                                    ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                    message.setCharType(client.getCharacterType());
                                    oos_client.writeObject(message);
                                    log("Notify game start from " + current.getCharacterInfo()
                                            + " to " + client.getCharacterInfo().getUsername()
                                    );
//                    client.setIsStarted(true);

                                }

                                message.setCommand(ExchangeMessage.SERVER_SEND_MESSAGE);
                                message.setContent("It's night time. Closes your eyes.\n"
                                        + " Werewolves are on hunting.");
                                for (ClientInfo client : clients) {
                                    OutputStream os_client = client.getSocket().getOutputStream();
                                    ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                    oos_client.writeObject(message);
                                    log("Notify night time"
                                            + " to " + client.getCharacterInfo().getUsername()
                                    );

                                }
                                for (ClientInfo client : clients) {
                                    if (client.getCharacterType() == ExchangeMessage.WEREWOLF_TYPE) {
                                        message.setCommand(ExchangeMessage.SERVER_NOTIFY_WEREWOLF);
                                        message.setContent("It's Your turn to hunt. Choose who to kill.");
                                        OutputStream os_client = client.getSocket().getOutputStream();
                                        ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                        oos_client.writeObject(message);
                                        log("Awake werewolf to" + client.getCharacterInfo().getUsername());
                                    } else if (client.getCharacterType() == ExchangeMessage.PROTETOR_TYPE) {
                                        message.setCommand(ExchangeMessage.SERVER_NOTIFY_PROTECTOR);
                                        message.setContent("It's Your turn to protect. Choose who to protecet.");
                                        OutputStream os_client = client.getSocket().getOutputStream();
                                        ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                        oos_client.writeObject(message);
                                        log("Awake protector to" + client.getCharacterInfo().getUsername());
                                    } else if (client.getCharacterType() == ExchangeMessage.SEER_TYPE) {
                                        message.setCommand(ExchangeMessage.SERVER_NOTIFY_SEER);
                                        message.setContent("It's Your turn to check. Choose who to check.");
                                        OutputStream os_client = client.getSocket().getOutputStream();
                                        ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                        oos_client.writeObject(message);
                                        log("Awake seer to" + client.getCharacterInfo().getUsername());
                                    }
//                                    else if (client.getCharacterType() == ExchangeMessage.WIZARD_TYPE) {
//                                        message.setCommand(ExchangeMessage.SERVER_SEND_VICTIM_TO_WIZARD);
//                                        message.setContent("It's Your turn to check. Choose who to check.");
//                                        OutputStream os_client = client.getSocket().getOutputStream();
//                                        ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
//                                        oos_client.writeObject(message);
//                                        log("Awake wizard to" + client.getCharacterInfo().getUsername());
//                                    }
                                }
                            }
                            break;

                    }

                }
                gameStart(os, oos);

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public void gameStart(OutputStream os, ObjectOutputStream oos) throws IOException {
            while (!gameOver) {
                nightAction(os, oos);
                if (checkGameOver()) {
                    ExchangeMessage message = new ExchangeMessage();
                    message.setContent("GAMEOVER. WEREWOLVES WON");
                    if (getWolfNumber() == 0) {
                        message.setContent("GAMEOVER. HUMAN WON");
                        message.setCommand(ExchangeMessage.SERVER_NOTIFY_HUMAN_WIN);
                    } else {
                        message.setCommand(ExchangeMessage.SERVER_NOTIFY_WOLF_WIN);
                    }
                    for (ClientInfo client : clients) {
                        OutputStream os_client = client.getSocket().getOutputStream();
                        ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                        oos_client.writeObject(message);
                    }
                    return;
                }
                voteList.removeAll(voteList);
                maxVote = 0;
                hanger = null;
                voted = 0;
                alive = updateAlive();

                dayAction(os, oos);
                if (checkGameOver()) {
                    ExchangeMessage message = new ExchangeMessage();
                    message.setContent("GAMEOVER. WEREWOLVES WON");
                    if (getWolfNumber() == 0) {
                        message.setContent("GAMEOVER. HUMAN WON");
                        message.setCommand(ExchangeMessage.SERVER_NOTIFY_HUMAN_WIN);
                    } else {
                        message.setCommand(ExchangeMessage.SERVER_NOTIFY_WOLF_WIN);
                    }
                    for (ClientInfo client : clients) {
                        OutputStream os_client = client.getSocket().getOutputStream();
                        ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                        oos_client.writeObject(message);
                    }
                    return;
                }
            }

        }

        public void dayAction(OutputStream os, ObjectOutputStream oos) throws IOException {
            ExchangeMessage message = new ExchangeMessage();

            for (ClientInfo client : clients) {
                String content = "It's day time. Wake up villagers. Last night ";
                if (isProtected == false && victim != null) {
                    content = content.concat(", " + victim + " is killed");
                    message.setTarget(victim);
                    if (client.getCharacterInfo().getUsername().equalsIgnoreCase(victim.getUsername())) {
                        client.setDead(true);
                    }
                }
                if (unlucky != null && !unlucky.getUsername().equalsIgnoreCase(victim.getUsername())) {
                    content = content.concat(", " + unlucky + " is killed");
                    message.setReceiver(unlucky);
                    if (client.getCharacterInfo().getUsername().equalsIgnoreCase(unlucky.getUsername())) {
                        client.setDead(true);
                    }

                }
                if (unlucky == null && isProtected == true) {
                    content = content.concat(", nobody is killed.");
                }
                message.setCommand(ExchangeMessage.SERVER_NOTIFY_DAY_BEGIN);
                content = content.concat(" " + updateAlive() + " is alive");
                message.setContent(content);
                if (client.isAwaken() == false) {
                    OutputStream os_client = client.getSocket().getOutputStream();
                    ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                    oos_client.writeObject(message);
                    log("Awake " + client.getCharacterInfo().getUsername());
                    client.setAwaken(true);
                    client.setIsStarted(false);
                }
            }
            isProtected = false;
            victim = null;
            unlucky = null;

//            sendMessageToAll("Good morning");
            while (!morningOver) {
                try {
                    InputStream is = current.getSocket().getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    message = (ExchangeMessage) ois.readObject();
                    switch (message.getCommand()) {
                        case ExchangeMessage.CLIENT_SEND_VOTE:
                            CharacterInfo vote = new CharacterInfo();
                            vote = message.getTarget();
                            boolean exist = false;
                            for (int i = 0; i < voteList.size(); i++) {
                                if (voteList.get(i).getUsername().equalsIgnoreCase(vote.getUsername())) {
                                    voteList.get(i).addVote();
                                    System.out.println("before " + voted);
                                    voted++;
                                    System.out.println("after " + voted);
                                    for (ClientInfo client : clients) {
                                        if (client.getCharacterInfo().getUsername().equalsIgnoreCase(message.getSender().getUsername())) {
                                            message.setCommand(ExchangeMessage.SERVER_SEND_VOTE_SUCCESS);
                                            OutputStream os_client = client.getSocket().getOutputStream();
                                            ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                            oos_client.writeObject(message);
                                            log("Send vote success to " + client.getCharacterInfo().getUsername()
                                            );
                                        }

                                    }
                                    vote = voteList.get(i);
                                    exist = true;
                                    break;
                                }
                            }
                            if (!exist) {
                                vote.addVote();
                                System.out.println("before " + voted);
                                voted++;
                                System.out.println("after " + voted);
                                for (ClientInfo client : clients) {
                                    if (client.getCharacterInfo().getUsername().equalsIgnoreCase(message.getSender().getUsername())) {
                                        message.setCommand(ExchangeMessage.SERVER_SEND_VOTE_SUCCESS);
                                        OutputStream os_client = client.getSocket().getOutputStream();
                                        ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                        oos_client.writeObject(message);
                                        log("Send vote success to " + client.getCharacterInfo().getUsername()
                                        );
                                    }

                                }
                                voteList.add(vote);
                            }
                            if (vote.getNoVote() > maxVote) {
                                maxVote = vote.getNoVote();
                                hanger = vote;
                            }
                            log(vote + "is voted " + vote.getNoVote() + " times ");
                            System.out.println(voted + " " + alive);

                            break;

                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (voted == alive + 1) {
                    morningOver = true;
                    log("Morning Over." + hanger + " is killed");
                    message.setTarget(hanger);
                    message.setCommand(ExchangeMessage.SERVER_NOTIFY_NIGHT_BEGIN);
                    message.setContent("Morning Over " + hanger + " is killed");
                    for (ClientInfo client : clients) {
                        if (!client.isIsStarted()) {
                            if (client.getCharacterInfo().getUsername().equalsIgnoreCase(hanger.getUsername())) {
                                client.setDead(true);
                            }
                            OutputStream os_client = client.getSocket().getOutputStream();
                            ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                            oos_client.writeObject(message);
                            log("Notify night time to " + client.getCharacterInfo().getUsername());
                        }
                    }
                    return;

                }
            }
        }

        public void nightAction(OutputStream os, ObjectOutputStream oos) throws IOException {

            ExchangeMessage message = new ExchangeMessage();
//            sendMessageToAll();
            nightOver = false;
            while (nightOver == false) {
                try {
                    InputStream is = current.getSocket().getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    message = (ExchangeMessage) ois.readObject();
                    switch (message.getCommand()) {
                        case ExchangeMessage.WEREWOLF_SEND_VICTOM:
                            victim = message.getTarget();
                            wolfDone = true;
                            log(victim + " is killed");
                            for (ClientInfo client : clients) {
                                if (client.getCharacterType() == ExchangeMessage.WIZARD_TYPE) {
                                    message.setCommand(ExchangeMessage.SERVER_SEND_VICTIM_TO_WIZARD);
                                    message.setTarget(victim);
                                    message.setContent(victim + "Is killed. Do you want to protect?");
                                    OutputStream os_client = client.getSocket().getOutputStream();
                                    ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                    oos_client.writeObject(message);
                                    log("Send " + victim + " to " + client.getCharacterInfo().getUsername()
                                    );
                                }

                            }
                            break;
                        case ExchangeMessage.SEER_SEND_TARGET:
                            seerDone = true;
                            log(message.getTarget() + "is checked");
                            boolean isWolf;
                            check = message.getTarget();
                            if (getType(check) == ExchangeMessage.WEREWOLF_TYPE) {
                                isWolf = true;
                            } else {
                                isWolf = false;
                            }
                            message.setCommand(ExchangeMessage.SERVER_SEND_CHECK_RESULT);
                            if (isWolf == true) {
                                message.setContent(check + " IS WEREWOLF");
                            } else {
                                message.setContent(check + " IS NOT WEREWOLF");
                            }
                            for (ClientInfo client : clients) {
                                if (client.getCharacterType() == ExchangeMessage.SEER_TYPE) {
                                    OutputStream os_client = client.getSocket().getOutputStream();
                                    ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                    oos_client.writeObject(message);
                                    log("Return result"
                                            + " to " + client.getCharacterInfo().getUsername() + isWolf
                                    );
                                }

                            }
                            break;
                        case ExchangeMessage.PROTECTOR_SEND_TARGET:
                            protectorDone = true;
                            System.out.println(victim);
                            protect = message.getTarget();
                            if (protect.getUsername().equalsIgnoreCase(victim.getUsername())) {
                                isProtected = true;
                            } else {
                                isProtected = false;
                            }
                            log(message.getTarget() + "is protected");
                            log("" + victim + isProtected);
                            break;
                        case ExchangeMessage.WIZARD_SEND_CHOICE_TO_SERVER:
                            wizardDone = true;
                            unlucky = message.getTarget();
                            isProtected = message.isIsProtected();
                            System.out.println("Wizard choose to kill " + unlucky + " Protect: " + isProtected);
                        case ExchangeMessage.CLIENT_SEND_ALL_CHAT:
                            message.setCommand(ExchangeMessage.SERVER_NOTIFY_ALL_CHAT);
                            String content = message.getContent();
                            message.setContent(message.getSender()+": " + content);
                            for(ClientInfo client: clients){
                                OutputStream os_client = client.getSocket().getOutputStream();
                                    ObjectOutputStream oos_client = new ObjectOutputStream(os_client);
                                    oos_client.writeObject(message);
                                    log("Chat all sended");
                            }
                            break;

                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (wolfDone == true && protectorDone == true && wizardDone == true && seerDone == true) {
                    nightOver = true;
                    wolfDone = false;
                    protectorDone = false;
                    wizardDone = false;
                    seerDone = false;
                    log("Night Over");
                    return;
                }
            }

        }

        public Socket getReceiverConnection(CharacterInfo user) {
            for (ClientInfo client : clients) {
                if (client.getCharacterInfo().getUsername().equals(user.getUsername())) {
                    return client.getSocket();
                }
            }
            return null;
        }

    }

    int getType(CharacterInfo ci) {
        int type = -1;
        for (ClientInfo client : clients) {
            if (client.getCharacterInfo().getUsername().equalsIgnoreCase(ci.getUsername())) {
                type = client.getCharacterType();
            }
        }

        return type;
    }

    public void log(String message) {
        System.out.println(message);
    }

    public int updateAlive() {
        alive = 0;
        for (ClientInfo client : clients) {
            if (!client.isDead()) {
                alive++;
            }
        }
        return alive;
    }

    public int getWolfNumber() {
        int count = 0;
        for (ClientInfo client : clients) {
            if (client.getCharacterType() == ExchangeMessage.WEREWOLF_TYPE&&!client.isDead()) {
                count++;
            }
        }
        return count;
    }

    public boolean checkGameOver() {

        if (updateAlive() > getWolfNumber() * 2 && getWolfNumber() != 0) {
            gameOver = false;
        } else {
            gameOver = true;
        }
        return gameOver;
    }
}
