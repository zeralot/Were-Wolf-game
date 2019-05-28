/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Character.CharacterInfo;
import Message.ExchangeMessage;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

/**
 *
 * @author Be Khanh Duy
 */
public class gameScreen extends javax.swing.JFrame {

    /**
     * Creates new form gameScreen
     */
    HashMap<CharacterInfo, ChatDialog> onlines = new HashMap<>();
    Socket socket;
    CharacterInfo user;
    CharacterInfo target;
    MainScreen ms;
    boolean wizardProtect;
    boolean dead = false;
    boolean night;

    public gameScreen(HashMap<CharacterInfo, ChatDialog> onlines, Socket socket, CharacterInfo user, MainScreen ms) {
        this.onlines = onlines;
        this.socket = socket;
        this.user = user;
        this.ms = ms;
        initComponents();
        wizardProtect = false;
        btnWizard.setVisible(false);
        setRole();
        this.setTitle(user.getUsername());
        loadOnlineUser();
    }

    private void loadOnlineUser() {
        //lstUsers.setModel(null);
        DefaultListModel model = new DefaultListModel();
        DefaultComboBoxModel model1 = new DefaultComboBoxModel();
        for (Map.Entry<CharacterInfo, ChatDialog> entry : onlines.entrySet()) {
            CharacterInfo key = entry.getKey();
            key.setReady(false);
            model.addElement(key);
            model1.addElement(key);
        }
        lstUsers.setModel(model);
        cbxUsers.setModel(model1);
    }

    public void setRole() {
        ImageIcon iconWerewolf = new ImageIcon("E:\\werewolf.jpg");
        ImageIcon iconProtector = new ImageIcon("E:\\protector.jpg");
        ImageIcon iconSeer = new ImageIcon("E:\\Seer.jpg");
        ImageIcon iconWizard = new ImageIcon("E:\\wizard.jpg");
        ImageIcon iconVillager = new ImageIcon("E:\\Villager.jpg");
        int role = user.getCharType();
        if (role == ExchangeMessage.WEREWOLF_TYPE) {
            this.role.setText("WEREWOLF");
            this.lblIcon.setIcon(iconWerewolf);
        } else if (role == ExchangeMessage.PROTETOR_TYPE) {
            this.role.setText("PROTECTOR");
            this.lblIcon.setIcon(iconProtector);
        } else if (role == ExchangeMessage.SEER_TYPE) {
            this.role.setText("SEER");
            this.lblIcon.setIcon(iconSeer);
        } else if (role == ExchangeMessage.WIZARD_TYPE) {
            this.role.setText("WIZARD");
            this.lblIcon.setIcon(iconWizard);
        } else if (role == ExchangeMessage.VILLAGE_TYPE) {
            this.role.setText("VILLAGER");
            this.lblIcon.setIcon(iconVillager);
        }
    }

    public void setServerMessage(String content) {
        txtDiary.setText(txtDiary.getText() + "\n" + "Server: "+content);
    }
    public void setAllChat(String content){
        txtDiary.setText(txtDiary.getText() + "\n" +content);
    }

    public void nightAwake() {
        night = true;
        if (user.getCharType() == ExchangeMessage.WEREWOLF_TYPE) {
            btnAction.setText("Kill");
        } else if (user.getCharType() == ExchangeMessage.PROTETOR_TYPE) {
            btnAction.setText("Protect");
        } else if (user.getCharType() == ExchangeMessage.SEER_TYPE) {
            btnAction.setText("Check");
        } else if (user.getCharType() == ExchangeMessage.WIZARD_TYPE) {
            btnAction.setText("Kill");
            if(!wizardProtect)
            btnWizard.setVisible(true);
            btnWizard.setText("Protect");
        }

    }
    
    
    
    public void dayAwake() {
        night = false;
        if (this.dead != true) {
            btnAction.setVisible(true);
            cbxUsers.setVisible(true);
            btnWizard.setVisible(false);
            btnAction.setText("Vote");
        }
    }

    public void deleteDead(CharacterInfo dead) {
        if (dead == null) {
            return;
        }
        if (user.getUsername().equalsIgnoreCase(dead.getUsername())) {
            btnAction.setVisible(false);
            cbxUsers.setVisible(false);
            btnSend.setVisible(false);
            btnWizard.setVisible(false);
            this.dead = true;
            System.out.println("You are dead");
        }
            try {
                for (Map.Entry<CharacterInfo, ChatDialog> entry : onlines.entrySet()) {
                    CharacterInfo key = entry.getKey();
                    if (key.getUsername().equalsIgnoreCase(dead.getUsername())) {
                        onlines.remove(key);
                    }
                }
            } catch (Exception e) {

            }
        
        loadOnlineUser();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        role = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstUsers = new javax.swing.JList();
        txtChat = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnAction = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiary = new javax.swing.JTextArea();
        cbxUsers = new javax.swing.JComboBox();
        btnWizard = new javax.swing.JButton();
        lblIcon = new javax.swing.JLabel();

        jButton3.setText("jButton3");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        role.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        role.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        role.setText("Role");

        jScrollPane1.setViewportView(lstUsers);

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnAction.setText("Action");
        btnAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionActionPerformed(evt);
            }
        });

        txtDiary.setColumns(20);
        txtDiary.setRows(5);
        jScrollPane2.setViewportView(txtDiary);

        btnWizard.setText("Action");
        btnWizard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWizardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(cbxUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAction)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnWizard)))
                .addGap(59, 59, 59))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(183, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(role, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(role, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(lblIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAction, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxUsers)
                    .addComponent(btnWizard, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionActionPerformed
        // TODO add your handling code here:

        
        CharacterInfo victim = (CharacterInfo) cbxUsers.getSelectedItem();
        this.target = victim;
        if(night){
        if (user.getCharType() == ExchangeMessage.WIZARD_TYPE) {
            ms.sendWizardChoice(target, wizardProtect);
//            btnAction.setVisible(false);
//            cbxUsers.setVisible(false);
        } else {
            ms.sendTargetInfo(target);
        }}
        else {
            ms.sendVote(target);
        }


    }//GEN-LAST:event_btnActionActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        String content = txtChat.getText();
        ms.chatAll(content);
        txtChat.setText("");
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnWizardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWizardActionPerformed
        // TODO add your handling code here:
        wizardProtect = true;
        btnWizard.setVisible(false);
    }//GEN-LAST:event_btnWizardActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new gameScreen().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAction;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnWizard;
    private javax.swing.JComboBox cbxUsers;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JList lstUsers;
    private javax.swing.JLabel role;
    private javax.swing.JTextField txtChat;
    private javax.swing.JTextArea txtDiary;
    // End of variables declaration//GEN-END:variables
}
