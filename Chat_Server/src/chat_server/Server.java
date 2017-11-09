package chat_server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends javax.swing.JFrame 
{
   ArrayList FluxSortantClient;
   ArrayList<String> utilisateurs;

   public class GestionClient implements Runnable	
   {
       //Déclaration des variables
       BufferedReader bufferedR;
       Socket socket;
       PrintWriter client;

       //Ajout des clients ?
       public GestionClient(Socket clientSocket, PrintWriter utilisateurs) 
       {
            client = utilisateurs;
            try 
            {
                socket = clientSocket;
                InputStreamReader entree = new InputStreamReader(socket.getInputStream());
                bufferedR = new BufferedReader(entree);
            }
            catch (Exception ex) 
            {
                ContenuChat.append("Erreur... \n");
            }

       }

       @Override
       public void run() 
       {
            //Déclaration et instanciation des variables
            String message, connect = "Connect", chat = "Chat" ;
            String[] donnees;

            try 
            {
                //Tant qu'on lit des données
                while ((message = bufferedR.readLine()) != null) 
                {
                    ContenuChat.append("Reçu: " + message + "\n");
                    //Séparation des infos contenu dans le message séparées par ":"
                    // on parse le message
                    donnees = message.split(":");
                    
                    if (donnees[2].equals(connect)) 
                    {
                        diffusionGenerale((donnees[0] + ":" + donnees[1] + ":" + chat));
                    } 
                    else if (donnees[2].equals(chat)) 
                    {
                        diffusionGenerale(message);
                    } 
                    else 
                    {
                        ContenuChat.append("Erreur... \n");
                    }
                } 
             } 
             catch (Exception ex) 
             {
                ContenuChat.append("Connexion perdue. \n");
                ex.printStackTrace();
                FluxSortantClient.remove(client);
             } 
	} 
    }

    public Server() 
    {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ContenuChat = new javax.swing.JTextArea();
        b_start = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server"); // NOI18N
        setResizable(false);

        ContenuChat.setColumns(20);
        ContenuChat.setRows(5);
        jScrollPane1.setViewportView(ContenuChat);

        b_start.setText("START");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 177, Short.MAX_VALUE)
                        .addComponent(b_start, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(172, 172, 172)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(b_start, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_startActionPerformed
        Thread starter = new Thread(new DemarageServeur());
        starter.start();
        
        ContenuChat.append("Serveur démarré...\n");
    }//GEN-LAST:event_b_startActionPerformed

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new Server().setVisible(true);
            }
        });
    }
    
    public class DemarageServeur implements Runnable 
    {
        @Override
        public void run() 
        {
            FluxSortantClient = new ArrayList();
            utilisateurs = new ArrayList();  

            try 
            {
                ServerSocket serverSock = new ServerSocket(2205);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
				PrintWriter PrintW = new PrintWriter(clientSock.getOutputStream());
				FluxSortantClient.add(PrintW);

				Thread ecoute = new Thread(new GestionClient(clientSock, PrintW));
				ecoute.start();
				ContenuChat.append("Connexion avec un nouveau client établie. \n");
                }
            }
            catch (Exception ex)
            {
                ContenuChat.append("Erreur a la connexion. \n");
            }
        }
    }
    
    
    public void diffusionGenerale(String message) 
    {
	Iterator it = FluxSortantClient.iterator();

        while (it.hasNext()) 
        {
            try 
            {
                PrintWriter printerW = (PrintWriter) it.next();
		printerW.println(message);
		ContenuChat.append("Envoyé: " + message + "\n");
                printerW.flush();
                ContenuChat.setCaretPosition(ContenuChat.getDocument().getLength());

            } 
            catch (Exception ex) 
            {
		ContenuChat.append("Erreur dans l'annonce. \n");
            }
        } 
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea ContenuChat;
    private javax.swing.JButton b_start;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
