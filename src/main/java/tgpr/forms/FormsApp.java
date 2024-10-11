package tgpr.forms;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Window;
import tgpr.forms.view.*;
import tgpr.forms.controller.*;
import tgpr.forms.model.*;
import tgpr.forms.controller.FormCardController;
import tgpr.framework.Controller;
import tgpr.framework.Model;

import java.util.ArrayList;
import java.util.List;

public class FormsApp {
    public final static String DATABASE_SCRIPT_FILE = "/database/tgpr-2425-a01.sql";

    public static void main(String[] args) {
        if (!Model.checkDb(DATABASE_SCRIPT_FILE))
            Controller.abort("Database is not available!");
        else {
            List<Form> testForms = new ArrayList<>();
            testForms.add(new Form("Form 1", "Description of Form 1", new User("John Doe"))); // Remplacez User par la classe appropriée
            testForms.add(new Form("Form 2", "Description of Form 2", new User("Jane Smith"))); // Assurez-vous que la classe User existe

            // Initialisation du contrôleur avec la liste des formulaires
            FormsViewController formsViewController = new FormsViewController(testForms);

            // Initialisation de la vue des formulaires
            FormsView formsView = new FormsView(formsViewController);

            // Configuration de la fenêtre
            TerminalSize size = new TerminalSize(80, 24); // Taille de la fenêtre Lanterna
            Window window = new Window("Forms Application", size);
            window.setComponent(formsView.getMainPanel());

            // Lancement de l'application
            Controller.navigateTo(window);
        }
    }


}
