/**
 *
 * @author pachayo
 * Avec l'aide de Baudouin des Vaux & Kerian Noel
 */
package calculator;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {

        // Declaration des variables
        boolean attente = true;
        Socket socketClient;
        BufferedReader entree;
        Scanner sc;
        OutputStream fluxSortie;
        ObjectOutputStream objetSortie;

        try {
            
            // On cree le socket qui va etre envoye au serveur
            socketClient = new Socket("localhost", 35160);

            // On declare le buffer d entree tapee par le client
            entree = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            
            
            //
            sc = new Scanner(System.in);

            // On ouvre un flux de sortie
            fluxSortie = socketClient.getOutputStream();

            // On cree l objet qui passera par le flux de sortie
            objetSortie = new ObjectOutputStream(fluxSortie);
            
            
            System.out.print("Cette calculette ne prend pas en charge les flux de sortie \n");
            
            //Boucle pour le calcul
            while (attente) {
                System.out.print("Calcul:");
                
                // Le calcul du client est stocke dans le variable calculClient
                String calculClient = sc.nextLine();

                // Lorsque calculClient vaut "Stop" on passe attente a false et sort de la boucle
                if (calculClient.equals("Stop")) {
                    attente = false;
                    
                    //
                    objetSortie.reset();
                    
                    //
                    objetSortie.writeBoolean(attente);
                    
                    // On envoie l'objet
                    objetSortie.flush();

                    // On attend le retour du servuer
                    String result = entree.readLine();
                    
                    // On affiche dans la console la reponse du serveur
                    System.out.print(result + "\n");

                    // On vide la mémoire
                    objetSortie.close();
                    entree.close();
                    
                    // On ferme la connexion
                    socketClient.close();
                } else {
                    
                    objetSortie.reset();
                    Serialisation calc = new Serialisation(calculClient);
                    objetSortie.writeBoolean(attente);
                    objetSortie.writeObject(calc);
                    
                    // Envoie de l objet au serveur
                    objetSortie.flush();

                    // On attend la reponse du servuer
                    String result = entree.readLine();
                    // On affiche la reponse du serveur
                    System.out.print("Resultat: " + result + "\n");
                }

            }

        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}