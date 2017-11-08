package chat_client;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client extends javax.swing.JFrame 
{
    //Déclaration et instanciation des variables
    String pseudo, address = "localhost";
    ArrayList<String> utilisateurs = new ArrayList();
    int port = 2205;
    Boolean isConnected = false;
    
    Socket socket;
    //lire la reception du serv
    BufferedReader bufferedR;
    //envoyer au serv
    PrintWriter printW;
    
    //--------------------------//
    
    public void ecouteThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
    }
    
    //--------------------------//
    
    public void ajoutUtilisateur(String donnees) 
    {
         utilisateurs.add(donnees);
    }
    
    //--------------------------//
    
    public void suppressionU(String donnees) 
    {
         ContenuChat.append(donnees + " est déconnecté.\n");
    }
    
    //--------------------------//
    
    public void listeU() 
    {
         String[] tempList = new String[(utilisateurs.size())];
         utilisateurs.toArray(tempList);
         for (String token:tempList) 
         {
             //Debug pour voir les participants
             //ContenuChat.append(token + "\n");
         }
    }
    
    //--------------------------//
    
    public void sendDisconnect() 
    {
        String bye = (pseudo + ": :Disconnect");
        try
        {
            printW.println(bye); 
            printW.flush(); 
        } catch (Exception e) 
        {
            ContenuChat.append("Impossible d'envoyer la deconnexion.\n");
        }
    }

    //--------------------------//
    
    public void Disconnect() 
    {
        try 
        {
            ContenuChat.append("Deconnecte.\n");
            socket.close();
        } catch(Exception ex) {
            ContenuChat.append("Erreur dans la deconnexion. \n");
        }
        isConnected = false;
        //?????
        champs_utilisateur.setEditable(true);

    }
    
    public Client() 
    {
        initComponents();
    }
    
    //--------------------------//
    
    public class IncomingReader implements Runnable
    {
        @Override
        // reecrite car il y a deja une def de la fonction run
        public void run() 
        {
            String[] donnees;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

            try 
            {
                while ((stream = bufferedR.readLine()) != null) 
                {
                    // on separe a tout les :
                     donnees = stream.split(":");

                     if (donnees[2].equals(chat)) 
                     {
                        ContenuChat.append(donnees[0] + ": " + donnees[1] + "\n");
                        
                        //permet de scroll automatiquement avec la taille totale qu il y a dans le chat
                        ContenuChat.setCaretPosition(ContenuChat.getDocument().getLength());
                     } 
                     else if (donnees[2].equals(connect))
                     {
                        ajoutUtilisateur(donnees[0]);
                     } 
                     else if (donnees[2].equals(disconnect)) 
                     {
                         suppressionU(donnees[0]);
                     } 
                     else if (donnees[2].equals(done)) 
                     {
                        listeU();
                        utilisateurs.clear();
                     }
                }
           }catch(Exception ex) { }
        }
    }

    //--------------------------//
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        libellé_utilisateur = new javax.swing.JLabel();
        champs_utilisateur = new javax.swing.JTextField();
        b_connect = new javax.swing.JButton();
        b_disconnect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ContenuChat = new javax.swing.JTextArea();
        champs_chat = new javax.swing.JTextField();
        b_send = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's frame");
        setName("client"); // NOI18N
        setResizable(false);

        libellé_utilisateur.setText("Pseudo :");

        champs_utilisateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                champs_utilisateurActionPerformed(evt);
            }
        });

        b_connect.setText("Connect");
        b_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_connectActionPerformed(evt);
            }
        });

        b_disconnect.setText("Disconnect");
        b_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_disconnectActionPerformed(evt);
            }
        });

        ContenuChat.setColumns(20);
        ContenuChat.setRows(5);
        jScrollPane1.setViewportView(ContenuChat);

        b_send.setText("SEND");
        b_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(champs_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(libellé_utilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(champs_utilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(184, 184, 184)
                        .addComponent(b_connect)
                        .addGap(2, 2, 2)
                        .addComponent(b_disconnect)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_connect)
                    .addComponent(b_disconnect)
                    .addComponent(libellé_utilisateur)
                    .addComponent(champs_utilisateur))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(champs_chat))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void champs_utilisateurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_champs_utilisateurActionPerformed
    
    }//GEN-LAST:event_champs_utilisateurActionPerformed

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_connectActionPerformed
        if (isConnected == false) 
        {
            //on prend la valeur pour le nm de l user
            pseudo = champs_utilisateur.getText();
            //on grise le champ utilisateur
            champs_utilisateur.setEditable(false);

            try 
            {
                socket = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(socket.getInputStream());
                bufferedR = new BufferedReader(streamreader);
                printW = new PrintWriter(socket.getOutputStream());
                printW.println(pseudo + ":est connecté.:Connect");
                printW.flush(); 
                isConnected = true; 
            } 
            catch (Exception ex) 
            {
                ContenuChat.append("Impossible de se connecter! \n");
                champs_utilisateur.setEditable(true);
            }
            
            // On creer et start un thread
            ecouteThread();
            
        } else if (isConnected == true) 
        {
            ContenuChat.append("Vous êtes déjà connecté. \n");
        }
    }//GEN-LAST:event_b_connectActionPerformed

    private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_disconnectActionPerformed
        sendDisconnect();
        Disconnect();
    }//GEN-LAST:event_b_disconnectActionPerformed

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sendActionPerformed
        String temp = "";
        if ((champs_chat.getText()).equals(temp)) {
            champs_chat.setText("");
            champs_chat.requestFocus();
        } else {
            try {
               printW.println(pseudo + ":" + champs_chat.getText() + ":" + "Chat");
               printW.flush(); // flushes the buffer
            } catch (Exception ex) {
                ContenuChat.append("Le message n a pas ete envoye. \n");
            }
            champs_chat.setText("");
            //Focus --> le curseur retourne dans la zone d ecriture le message
            champs_chat.requestFocus();
        }

        champs_chat.setText("");
        champs_chat.requestFocus();
    }//GEN-LAST:event_b_sendActionPerformed

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new Client().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea ContenuChat;
    private javax.swing.JButton b_connect;
    private javax.swing.JButton b_disconnect;
    private javax.swing.JButton b_send;
    private javax.swing.JTextField champs_chat;
    private javax.swing.JTextField champs_utilisateur;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JLabel libellé_utilisateur;
    // End of variables declaration//GEN-END:variables
}
