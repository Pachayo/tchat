package calculette;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {

		Socket socket;
		BufferedReader in;
		OutputStream outStream;
		ObjectOutputStream out;
		Scanner sc;
		boolean waiting = true;

		try {
			// Cr�ation du socket qui va envoy� un demande de connection
			socket = new Socket("localhost", 2009);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outStream = socket.getOutputStream();
			out = new ObjectOutputStream(outStream);
			sc = new Scanner(System.in);

			while (waiting) {
				System.out.print("Votre calcul svp:");
				String calcul = sc.nextLine();

				// Si le client envoie Over le client va aussi se fermer comme pour le serveur
				if (calcul.equals("Over")) {
					waiting = false;
					
					out.reset();
					out.writeBoolean(waiting);
					out.flush();
					
					// Attend la r�ponse du servuer
					String result = in.readLine();
					// Affiche la r�ponse du serveur
					System.out.print(result + "\n");
					
					out.close();
					in.close();
					socket.close();
				} else {
					out.reset();
					Calculus calc = new Calculus(calcul);
					out.writeBoolean(waiting);
					out.writeObject(calc);
					out.flush();
					
					// Attend la r�ponse du servuer
					String result = in.readLine();
					// Affiche la r�ponse du serveur
					System.out.print(result + "\n");
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
