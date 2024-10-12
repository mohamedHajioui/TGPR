package tgpr.forms;

import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import tgpr.forms.controller.ViewFormsController;
import tgpr.forms.model.User;
import tgpr.framework.Controller;
import tgpr.framework.Model;

public class FormsApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2425-a01.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE)) {
            Controller.abort("Database is not available!");
        } else {
            try {
                // Utiliser DefaultTerminalFactory pour créer le terminal
                DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
                Terminal terminal = terminalFactory.createTerminal();

                // Utiliser TerminalScreen pour initialiser l'écran
                Screen screen = new TerminalScreen(terminal);
                screen.startScreen();

                // Récupérer l'utilisateur de test (tu peux changer l'email selon tes besoins)
                User currentUser = User.getByEmail("bepenelle@epfc.eu");

                if (currentUser != null) {
                    // Lancer le contrôleur pour afficher les formulaires accessibles à cet utilisateur
                    new ViewFormsController(currentUser, screen);
                } else {
                    System.out.println("Utilisateur non trouvé.");
                }

                // Fermer proprement l'écran après l'affichage
                screen.stopScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}