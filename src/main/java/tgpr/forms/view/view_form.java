package tgpr.forms.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import tgpr.forms.controller.formController;
import tgpr.forms.model.*;
import tgpr.framework.ViewManager;

import java.util.List;

import static tgpr.framework.Tools.ifNull;

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
    private final Form form;
    private boolean normal;

    private final Button nq_button = new Button("New Question");
    private final Button ef_button = new Button("Edit Form");
    private final Button df_button = new Button("Delete Form");
    private final Button s_button = new Button("Share");
    private final Button r_button = new Button("Reorder");
    private final Button a_button = new Button("Analyse");

    private ObjectTable<Question> table;


    public view_form(formController controller,Form form,boolean normal) {
        super("View Form detail");

        this.controller = controller;
        this.form = form;
        this.normal = normal;



        affichage(normal);
    }

    public void affichage(boolean normal) {
        setHints(List.of(Hint.CENTERED,Hint.MODAL));
        setCloseWindowWithEscape(true);
        Panel root = Panel.verticalPanel();
        setComponent(root);

        upperDisciption().addTo(root);
        questionList().addTo(root);
        if (normal){
            createButtonsNormal().addTo(root);
        }else createButtonsReorder().addTo(root);
    }

    private Panel title(){
        Panel panel = Panel.gridPanel(2);
        new Label("Title: ").addTo(panel);
        new Label(form.getTitle()).addTo(panel);
        return panel;
    }

    private Panel description(){
        Panel panel = Panel.gridPanel(2);
        new Label("Description: ").addTo(panel);
        new Label(form.getDescription()).addTo(panel);
        return panel;
    }

    private Panel createdby(){
        Panel panel = Panel.gridPanel(2);
        new Label("Created by: ").addTo(panel);
        User user = form.getOwner();
        new Label(user.getFullName()).addTo(panel);
        return panel;
    }

    private Panel isPublic(){
        Panel panel = Panel.gridPanel(2);
        new Label("IS public: ").addTo(panel);
        new Label(form.getIsPublic()+"").addTo(panel);
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



    private Component questionList(){
        System.out.println("questionList");
        table = new ObjectTable<Question>(
                new ColumnSpec<>("index title", Question::getIdx),
                new ColumnSpec<>("Type", Question::getType),
                new ColumnSpec<>("Required", Question::getRequired),
                new ColumnSpec<>("Option List", q -> ifNull(q.getOptionList(),""))
        );
        table.sizeTo(ViewManager.getTerminalColumns(),15);
        table.add(form.getQuestions());

        if (!normal){
            reOrder();
        }
        return table;
    }







    private Panel createButtonsNormal(){
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Nouvelle Question").addTo(panel);
        new Button("Edit Form").addTo(panel);
        new Button("Delete Form", this::delete).addTo(panel);
        new Button("Share").addTo(panel);
        new Button("Reorder",this::reOrder).addTo(panel);
        new Button("Analyse").addTo(panel);
        new Button("Cancel", this::close).addTo(panel);

        return panel;
    }

    private Panel createButtonsReorder(){
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Save Order").addTo(panel);
        new Button("Cancel", this::returnNormal).addTo(panel);
        return panel;
    }

    private void returnNormal(){
        normal = true;
        affichage(true);
    }

    private void delete() {
        controller.delete();
    }




    //1er etape: Rend l'affichage en mode reOrder avec les boutton save et cancel

    private void reOrder() {
        System.out.println("reOrder");
        if (normal){
            normal = false;
            affichage(false);
        }
        table.setSelectAction(this::choice);
    }


    // 2eme etape: attend que l'utilisateur apuisse sur enter pour commencer le swap
    private void choice(){
        System.out.println("choice");
        table.addSelectionChangeListener(this::selectionChanged);
        System.out.println("test");
    }

    // 3eme etape: faire le swap jusqu'au prochain appui du enter qui renvoie vers tmpsave
    private void selectionChanged(int prec, int current, boolean byUser) {
        System.out.println("sectionChanged");
        swap(prec, current);
        System.out.println("return");
        table.setSelectAction(this::questionList);
        choice();


    }

    private void swap(int prec, int current){
        System.out.println("swap");
        Question tmp = table.getItem(current);
        table.setItem(current, table.getItem(prec));
        table.setItem(prec, tmp);
        table.refresh();
    }

    private void endChoice(){
        affichage(false);
    }
}

