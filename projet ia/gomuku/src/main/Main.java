package main;

import page.PageAcceuil;
import javax.swing.JOptionPane;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//PageAcceuil page=new PageAcceuil();
		
		int response = JOptionPane.showConfirmDialog(null, "Voulez-vous jouer avec l'ordinateur ?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // Vérifie la réponse de l'utilisateur et affiche un message approprié
        if (response == JOptionPane.YES_OPTION) {
            System.out.println("L'utilisateur a cliqué sur Oui.");
            // Logique pour jouer avec l'ordinateur
        } else if (response == JOptionPane.NO_OPTION) {
            System.out.println("L'utilisateur a cliqué sur Non.");
            // Logique pour ne pas jouer
        } else {
            System.out.println("L'utilisateur a fermé la fenêtre de dialogue.");
            // Logique pour la fermeture de la fenêtre
        }
	}

}
