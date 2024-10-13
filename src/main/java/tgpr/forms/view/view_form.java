package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.AbstractWindow;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.TestController;
import tgpr.forms.controller.formController;
import tgpr.forms.model.Question;
import tgpr.forms.model.User;
import tgpr.framework.Controller;

import java.awt.*;
import java.util.List;

//import static jdk.internal.net.http.common.Utils.close;

/*
1 - Toute en haut a gauche on veu le message "View Form detail"

2- la partie superieure affichera:
                                Title : form.getTitle()
                                Description : form.getDiscription
                                Created by: user.Name
                                IS public : form.getIsPublic
3- la partie inferieure affichera:
                                 Index: question.getIdx
                                 Title: question.getTitle
                                 Type : question.getType
                                 Required : question.getRequired
                                 Option List: question.getOptionList
4 - les bouttons toute en bas:
                                -New Question
                                -Edit Form
                                -Delete Form
                                -Share
                                -Reorder
                                -Analyse
                                -Close

 */
public class view_form extends DialogWindow{

    private final formController controller;

    private final Button nq_button = new Button("New Question");
    private final Button ef_button = new Button("Edit Form");
    private final Button df_button = new Button("Delete Form");
    private final Button s_button = new Button("Share");
    private final Button r_button = new Button("Reorder");
    private final Button a_button = new Button("Analyse");


    public view_form(formController controller) {
        super("View Form detail");

        this.controller = controller;

        setHints(List.of(Hint.CENTERED,Hint.MODAL));
        setCloseWindowWithEscape(true);


        Panel root = Panel.verticalPanel();
        setComponent(root);

        upperDisciption().addTo(root);
        middleSecion().addTo(root);
        createButtons().addTo(root);


    }

    private Panel title(){
        Panel panel = Panel.gridPanel(2);
        new Label("Title: ").addTo(panel);
        // new Label().addTo ,extraction des donner
        return panel;
    }

    private Panel description(){
        Panel panel = Panel.gridPanel(2);
        new Label("Description: ").addTo(panel);
        // new Label().addTo ,extraction des donner
        return panel;
    }

    private Panel createdby(){
        Panel panel = Panel.gridPanel(2);
        new Label("Created by: ").addTo(panel);
        // new Label().addTo ,extraction des donner
        return panel;
    }

    private Panel isPublic(){
        Panel panel = Panel.gridPanel(2);
        new Label("IS public: ").addTo(panel);
        // new Label().addTo ,extraction des donner
        return panel;

    }


    //La methode qui regroupe les placement des description de la partie superieure du panel
    private Panel upperDisciption(){
        Panel panel = Panel.gridPanel(1);
        title().addTo(panel);
        description().addTo(panel);
        createdby().addTo(panel);
        isPublic().addTo(panel);
        return panel;
    }

    private Panel middleSecion(){
        Panel panel = Panel.gridPanel(20,Margin.of(1));
        new Label("Index Title").addTo(panel);
        panel.addEmpty();
        panel.addEmpty();
        panel.addEmpty();
        panel.addEmpty();
        panel.addEmpty();
        panel.addEmpty();
        new Label("Type").addTo(panel);
        panel.addEmpty();
        panel.addEmpty();
        new Label("Required").addTo(panel);
        new Label("Option").addTo(panel);
        new Label("List").addTo(panel);
        return panel;
    }

    private Panel createButtons(){
        var panel = Panel.horizontalPanel().center();

        nq_button.setEnabled(false).addTo(panel).addListener(button -> newQuestion());
        ef_button.setEnabled(false).addTo(panel).addListener(button -> newQuestion());
        df_button.setEnabled(false).addTo(panel).addListener(button -> newQuestion());
        s_button.setEnabled(false).addTo(panel).addListener(button -> newQuestion());
        r_button.setEnabled(false).addTo(panel).addListener(button -> newQuestion());
        a_button.setEnabled(false).addTo(panel).addListener(button -> newQuestion());
        new Button("Cancel", this::close).addTo(panel);

        return panel;
    }

    private void newQuestion() {

    }

}
