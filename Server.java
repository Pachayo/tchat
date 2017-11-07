package calculette;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

	public static void main(String[] zero) {

		try {
			// D�claration du socket
			ServerSocket socket;
			Socket socketClient;
			
			socket = new ServerSocket(2009);
			boolean check = true;
			
			while(check) {
				// Teste pour savoir si le socket n'est pas d�j� utilis�
					// Cr�ation di socket en �coute
					System.out.println("A user just connected.");
					
					//Un client se connecte, on l'accepte
					socketClient = socket.accept();
					
					// C'est ici que l'on cr�e le thread
					Thread t = new Thread(new Calcul(socketClient));
					t.start();
			}
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Calcul implements Runnable {

	private Socket socket;
	private PrintWriter out;
	private ObjectInputStream in;
	private boolean waiting = true;

	public Calcul(Socket s) {
		this.socket = s;
	}

	public void run() {

		try {
			this.out = new PrintWriter(this.socket.getOutputStream()); // Les infos qui parte vers le client
			this.in = new ObjectInputStream(this.socket.getInputStream());// Les infos qui arrive
																								// vers le serveur
			while (waiting) {
				waiting = in.readBoolean();
				
				if(!waiting) {
					this.out.println("Successfully disconnected. Have a nice day. :)");
					this.out.close();
					this.in.close();
					this.socket.close();
					
				} else {
					Calculus calcul = (Calculus) in.readObject();
					// Si le calcul peut être désérialisé, on passe la condition
					if(calcul.isDeserializable()) {
						// Sinon on fais le calcul
						int result = parseByBracket(calcul);
						// Je sais pas pourqoui mais quand j'en met un le client ne re�ois rien
						// Ajoute a out les infos
						this.out.println("Le r�sultat est : " + result);
						// Envoie les infos
						this.out.flush();
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int doCalcul(String calcul) {

		calcul = calcul.replaceAll("\\s+", ""); // Supprime tous les espaces
		// Cr�e une liste : le premier nombre puis le premier signe puis le deuxi�me
		// nombre puis le deuxi�mpe signe etc...
		ArrayList<String> calculParts = new ArrayList<String>(
				Arrays.asList(calcul.split("((?<=[\\+\\-\\*\\/])|(?=[\\+\\-\\*\\/]))")));
		int first = 0; // C'est notre variable tampon piur les calcul
		// Cette boucle for va calculer que les op�ration avec * et / car ce sont des
		// op�rations prioritaires par rapport au + et -
		for (int i = 1; i < calculParts.size(); i++) {
			if (calculParts.get(i).equals("*")) {
				// Fais le calcul avec les chiffers avant et apr�s le signe
				first = Integer.parseInt(calculParts.get(i - 1)) * Integer.parseInt(calculParts.get(i + 1));
				// Remplace le chiffre avant le signe par le calcul fais au-dessus
				calculParts.set(i - 1, String.valueOf(first));
				// Supprime le signe et le chiffres apr�s le signe dans la liste
				calculParts.remove(i);
				calculParts.remove(i);
				// On a supprim� l'�l�ment point� par i on doit revenir a celui juste
				// avant i
				i--;
			} else if (calculParts.get(i).equals("/")) {
				first = Integer.parseInt(calculParts.get(i - 1)) / Integer.parseInt(calculParts.get(i + 1));
				calculParts.set(i - 1, String.valueOf(first));
				calculParts.remove(i);
				calculParts.remove(i);
				i--;
			}
		}
		// Cette boucle fais les calculs pur les + et -
		for (int i = 1; i < calculParts.size(); i++) {
			if (calculParts.get(i).equals("+")) {
				first = Integer.parseInt(calculParts.get(i - 1)) + Integer.parseInt(calculParts.get(i + 1));
				calculParts.set(i - 1, String.valueOf(first));
				calculParts.remove(i);
				calculParts.remove(i);
				i--;
			} else if (calculParts.get(i).equals("-")) {
				first = Integer.parseInt(calculParts.get(i - 1)) - Integer.parseInt(calculParts.get(i + 1));
				calculParts.set(i - 1, String.valueOf(first));
				calculParts.remove(i);
				calculParts.remove(i);
				i--;
			}
		}
		// Retoune le résultat
		return Integer.parseInt(calculParts.get(0));
	}

	public int parseByBracket(Calculus calcul) {
		// ((?<=[\\(])|(?=[)]))
		ArrayList<String> calculParts = calcul.getArray();
		String tempCalcul = "";
		int result = calcul.getResult();
		
		for (int i = 0; i < calculParts.size(); i++) {
			if (calculParts.get(i).equals(")")) {
				for (int j = i; j >= 0; j--) {
					tempCalcul += calculParts.get(j);
					if (calculParts.get(j).equals("(")) {
						tempCalcul = tempCalcul.substring(1);
						tempCalcul = tempCalcul.substring(0, tempCalcul.length() - 1);
						result = doCalcul(tempCalcul);
						calculParts.set(j, String.valueOf(result));
						int index = 1;
						do {
							calculParts.remove(j + 1);
							index++;
						} while (index <= (i - j));

						i -= (i - 1);
						j = -1;
						tempCalcul = "";
					}
				}
			}
		}
		for (int j = 0; j < calculParts.size(); j++) {
			tempCalcul += calculParts.get(j);
		}
		calculParts.set(0, String.valueOf(doCalcul(tempCalcul)));
		return Integer.parseInt(calculParts.get(0));
	}
}