package tgpr.forms;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import tgpr.forms.controller.EditInstanceController;
import tgpr.forms.model.Form;
import tgpr.forms.model.Instance;
import tgpr.forms.model.User;
import tgpr.forms.view.EditInstanceView;
import tgpr.framework.Controller;
import tgpr.framework.Model;

public class FormsApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2425-a01.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE)) {
            Controller.abort("Database is not available!");
        } else {
            try {
                // Initialisation du terminal et de l'écran
                DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
                Terminal terminal = terminalFactory.createTerminal();
                Screen screen = new TerminalScreen(terminal);
                screen.startScreen(); // Démarre l'écran

                // Récupère un utilisateur et un formulaire pour tester (remplace ces ID par des ID valides)
                User user = User.getByKey(1); // Utilisateur avec ID 1 (à remplacer par un ID valide)
                Form form = Form.getByKey(1); // Formulaire avec ID 1 (à remplacer par un ID valide)

                // Crée une instance avec le formulaire et l'utilisateur
                Instance instance = new Instance(form, user);

                // Initialise la vue avec l'écran et les questions du formulaire
                EditInstanceView editInstanceView = new EditInstanceView(screen, form.getQuestions());

                // Crée le contrôleur avec l'instance et la vue
                EditInstanceController editInstanceController = new EditInstanceController(instance, editInstanceView);

                // Tester les méthodes du contrôleur
                editInstanceController.start(); // Lance le contrôleur pour tester le flux de l'instance
                editInstanceController.nextQuestion(); // Teste la navigation vers la question suivante
                editInstanceController.previousQuestion(); // Teste la navigation vers la question précédente
                editInstanceController.submitInstance(); // Teste la soumission de l'instance

                // Arrête l'écran lorsque l'application se termine
                screen.stopScreen();
            } catch (Exception e) {
                e.printStackTrace();
                Controller.abort("An error occurred while starting the application.");
            }
        }
    }
}
