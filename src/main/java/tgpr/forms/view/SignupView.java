package tgpr.forms.view;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import tgpr.forms.controller.SignupController;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.SignupController;

public class SignupView extends DialogWindow {
    private final SignupController controller;
    private Panel mainPanel;


    public SignupView(SignupController controller) {
        super("Signup");
        this.controller = controller;

        //creation du panel
        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(100, 40));


    }


}
