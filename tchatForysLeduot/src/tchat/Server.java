/**
 *
 * @author pachayo
 * Avec l'aide de Baudouin des Vaux & Kerian Noel
 */

package calculator;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static void main(String[] zero) {

        try {
            // Declaration des variables
            ServerSocket socket;
            Socket socketClient;

            socket = new ServerSocket(35160);
            boolean verification = true;

            // On teste si le socket est utilise
            while(verification) {

                // Si un client se connecte
                socketClient = socket.accept();
                
                // 
                System.out.println("Un utilisateur s est connecte.");
                
                // on cree le thread
                Thread t = new Thread(new Calcul(socketClient));
                
                // On demarre le thread
                t.start();
            }

            socket.close();
            
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}

class Calcul implements Runnable {

    // Declaration des variables
    private Socket socket;
    private PrintWriter sortie;
    private ObjectInputStream entree;
    private boolean attente = true;

    public Calcul(Socket s) {
            this.socket = s;
    }

    
    public void run() {

        try {
            // Cette variable contient le resultat qui va etre envoye au client
            this.sortie = new PrintWriter(this.socket.getOutputStream());

            //Cette variable contient le calcul qui provient du client
            this.entree = new ObjectInputStream(this.socket.getInputStream());

            // Bouble pour tester la connexion du client.                                                                                                                                                                        // vers le serveur
            while (attente) {
                attente = entree.readBoolean();

                // Si la connexion s'arrete
                if(!attente) {
                    this.sortie.println("Client deconnecte");

                    //On vide de la memoire les variables
                    this.sortie.close();
                    this.entree.close();
                    this.socket.close();

                } else {
                    Serialisation calculSerialise = (Serialisation) entree.readObject();

                    // Si le calcul est désérialisable
                    if(calculSerialise.isDeserializable()) {
                        
                        // On fais le calcul
                        int result = doCalcul(calculSerialise);
                        
                        // On affiche dans resultatCalcul le resultat du calcul
                        this.sortie.println("Resultat: " + result);
                        
                        // On transmets les infos au client
                        this.sortie.flush();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
        }
    }

    public int doCalcul(Serialisation calcultmp) {
        
        // On recupere le String de l objet
        String calcul = calcultmp.getString();
        
        // On utilise la fonction replaceAll pour supprimer les espaces "\\s+"
        calcul = calcul.replaceAll("\\s+", "");
        
        // On cree une array list, avec en premiere place la premier nombre, puis le premiere signe puis le second nombre puis etc. Ainsi on va pouvoir traiter les multiplication et division en priorite
        ArrayList<String> calculSepare = new ArrayList<String>(Arrays.asList(calcul.split("((?<=[\\+\\-\\*\\/])|(?=[\\+\\-\\*\\/]))")));
        
        // Variable temporaire pour les calculs
        int tmpCalcul = 0;
        
        // Avec ce for on traite d'abord les mutiplication et les divisions
        for (int i = 1; i < calculSepare.size(); i++) {
            if (calculSepare.get(i).equals("*")) {
                
                // Des qu une * est detectee on fait le calcul avec le chiffre precedant et suivant le signe
                tmpCalcul = Integer.parseInt(calculSepare.get(i - 1)) * Integer.parseInt(calculSepare.get(i + 1));
                
                // On met le resultat de la multiplication a la place du premier nombre
                calculSepare.set(i - 1, String.valueOf(tmpCalcul));
                
                // Il faut egalement prendre soin de supprimer le deuxieme nombre et le signe *
                calculSepare.remove(i);
                calculSepare.remove(i);
                
                // on decremente "i" pour pouvoir refaire calcul correctement
                i--;
                
            } else if (calculSepare.get(i).equals("/")) {
                // meme logique que pour le signe *
                tmpCalcul = Integer.parseInt(calculSepare.get(i - 1)) / Integer.parseInt(calculSepare.get(i + 1));
                calculSepare.set(i - 1, String.valueOf(tmpCalcul));
                calculSepare.remove(i);
                calculSepare.remove(i);
                i--;
            }
    }
        // Avec ce for on traite d'abord les aditions et les soustractions
    for (int i = 1; i < calculSepare.size(); i++) {
        if (calculSepare.get(i).equals("+")) {
        // meme logique que pour le signe *
            tmpCalcul = Integer.parseInt(calculSepare.get(i - 1)) + Integer.parseInt(calculSepare.get(i + 1));
            calculSepare.set(i - 1, String.valueOf(tmpCalcul));
            calculSepare.remove(i);
            calculSepare.remove(i);
            i--;
        } else if (calculSepare.get(i).equals("-")) {
        // meme logique que pour le signe *
            tmpCalcul = Integer.parseInt(calculSepare.get(i - 1)) - Integer.parseInt(calculSepare.get(i + 1));
            calculSepare.set(i - 1, String.valueOf(tmpCalcul));
            calculSepare.remove(i);
            calculSepare.remove(i);
            i--;
        }
    }
        // On retoune le résultat
        return Integer.parseInt(calculSepare.get(0));
    }


}