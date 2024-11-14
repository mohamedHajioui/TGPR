package tgpr.forms.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
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


3 modes:
    1 - la vue normal avec une instance sauvegarder
    2 - la vue normal avec sans instance suvegarder
    3 - la vue reorder
 */
public class view_form extends DialogWindow{

    private final formController controller;
    private final Form form;
    private boolean moving;
    private boolean normal;
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
        root.setPreferredSize(new TerminalSize(100, 16));
        setComponent(root);

        upperDisciption().addTo(root);
        questionList().addTo(root);

        if (normal){
            if(form.isUsed()){
                createButtonsInstance().addTo(root);
            }
            else{
                createButtonsNormal().addTo(root);
            }
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
        panel.addEmpty();
        title().addTo(panel);
        description().addTo(panel);
        createdby().addTo(panel);
        isPublic().addTo(panel);
        if(form.isUsed()){
            Label label  = new Label("This form is read only beacause it has already been answered ("+form.getInstances().size()+"instance(s)).");
            label.setForegroundColor(TextColor.ANSI.BLUE);
            label.addTo(panel);
        }
        return panel;
    }



    private Component questionList(){
        System.out.println("questionList");
        form.reload();
        table = new ObjectTable<Question>(
                new ColumnSpec<>("index", Question::getIdx),
                new ColumnSpec<>("title",Question::getTitle),
                new ColumnSpec<>("Type", Question::getType),
                new ColumnSpec<>("Required", Question::getRequired),
                new ColumnSpec<>("Option List", q -> ifNull(q.getOptionList(),""))
        );
        table.sizeTo(ViewManager.getTerminalColumns(),10);
        table.add(form.getQuestions());

        //on peut que associer qu'une fois le handler
        table.setSelectAction(this::choice);
        table.addSelectionChangeListener(this::selectionChanged);
        if (!normal){
            reOrder();
        }
        return table;
    }



    private Panel createButtonsNormal(){
        form.reorderQuestions(table.getItems());
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Nouvelle Question",this::nouvelleQuestion).addTo(panel);
        new Button("Edit Form",this::EditForm).addTo(panel);
        new Button("Delete Form", this::delete).addTo(panel);
        new Button("Share",this::shares).addTo(panel);
        new Button("Reorder",this::reOrder).addTo(panel);
        new Button("Analyse",this::Analyse).addTo(panel);
        new Button("Cancel", this::backToForms).addTo(panel);

        return panel;
    }
    private void backToForms(){
        controller.versViewForms();
    }

    private void Analyse(){
        controller.versAnalyse();
    }

    private void EditForm(){
        controller.versEditForm();
        affichage(true);
    }

    private void shares(){
        controller.versShare();
    }

    private void nouvelleQuestion(){
        controller.versNouvelleQuestion();
        affichage(true);
    }

    private Panel createButtonsReorder(){
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Save Order", this::save).addTo(panel);
        new Button("Cancel", this::returnNormal).addTo(panel);
        return panel;
    }

    private Panel createButtonsInstance(){
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Delete Form", this::delete).addTo(panel);
        if(form.getIsPublic()){
            new Button("Make Private", this::makePrivate).addTo(panel);
        }else new Button("Make Public", this::makePublic).addTo(panel);

        new Button("Clear Instances", this::clearInstances).addTo(panel);
        new Button("Analyse", this::Analyse).addTo(panel);
        new Button("Close", this::backToForms).addTo(panel);
        return panel;
    }

    private void clearInstances(){

        controller.versViewInstance();
    }

    private void makePublic(){
        controller.makePublic();
        form.setIsPublic(true);
        form.save();
        affichage(normal);

    }

    private void makePrivate(){
        form.setIsPublic(false);
        form.save();
        affichage(normal);
    }

    private void save(){
        System.out.println("save");
        form.reorderQuestions(table.getItems());
        normal = true;
        affichage(true);
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
        affichage(false);
        normal = false;

    }


    // 2eme etape: attend que l'utilisateur apuisse sur enter pour commencer le swap
    private void choice(){
        if (!normal){
            //on change juste l'etat de moving pour dire si on bouge ou paS
            moving = !moving;
        }else{
            controller.versEditQuestion(table.getSelected());
            affichage(true);
        }
        System.out.println("choice");
    }

    // 3eme etape: faire le swap jusqu'au prochain appui du enter qui renvoie vers tmpsave
    private void selectionChanged(int prec, int current, boolean byUser) {
        if (!moving) return;
        System.out.println("sectionChanged");
        swap(prec, current);
        System.out.println("return");


    }

    private void swap(int prec, int current){
        System.out.println("swap");
        Question tmp = table.getItem(current);
        table.setItem(current, table.getItem(prec));
        table.setItem(prec, tmp);
        table.refresh();
    }


}

