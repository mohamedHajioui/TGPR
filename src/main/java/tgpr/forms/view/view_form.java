package tgpr.forms.view;

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


public class view_form extends DialogWindow{

    private final formController controller;
    private final Form form;
    private boolean moving;
    private boolean normal;
    private ObjectTable<Question> table;

    private final Button nq_button = new Button("New Question");
    private final Button ef_button = new Button("Edit Form");
    private final Button df_button = new Button("Delete Form");
    private final Button s_button = new Button("Share");
    private final Button r_button = new Button("Reorder");
    private final Button a_button = new Button("Analyse");




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
            if(form.isUsed()){
                createButtonsInstance().addTo(root);
            }
            else{
                createButtonsNormal().addTo(root);
            }
        }else createButtonsReorder().addTo(root);
    }
//----------------------------------------------------------------------------------------------------------------------
//Affichage de la partie superieure du Panel

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


//----------------------------------------------------------------------------------------------------------------------
//Creation du tableau (Liste de question)


    private Component questionList(){
        System.out.println("questionList");
        form.reload();
        table = new ObjectTable<Question>(
                new ColumnSpec<>("index title", Question::getIdx),
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

//----------------------------------------------------------------------------------------------------------------------
//Les Bouttons pour un affichage par default

    private Panel createButtonsNormal(){
        form.reorderQuestions(table.getItems());
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Nouvelle Question",this::nouvelleQuestion).addTo(panel);
        new Button("Edit Form",this::EditForm).addTo(panel);
        new Button("Delete Form", this::delete).addTo(panel);
        new Button("Share",this::shares).addTo(panel);
        new Button("Reorder",this::reOrder).addTo(panel);
        new Button("Analyse",this::Analyse).addTo(panel);
        new Button("Cancel", this::closeForm).addTo(panel);

        return panel;
    }

    private void nouvelleQuestion(){
        controller.versNouvelleQuestion();
        affichage(true);
    }

    private void EditForm(){
        controller.versEditForm();
        affichage(true);
    }

    private void delete() {
        controller.delete();
    }

    private void shares(){
        controller.versShare();
    }


    private void Analyse(){
        controller.versAnalyse();
    }

    private void closeForm(){
        controller.versViewForms();
    }


//----------------------------------------------------------------------------------------------------------------------
//Affichage des bouttons en mode reOrder()



    private Panel createButtonsReorder(){
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Save Order", this::save).addTo(panel);
        new Button("Cancel", this::returnNormal).addTo(panel);
        return panel;
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

//----------------------------------------------------------------------------------------------------------------------
//Affichage si il existe des instances

    private Panel createButtonsInstance(){
        var panel = Panel.horizontalPanel().right().right().center();
        new Button("Delete Form", this::delete).addTo(panel);
        if(form.getIsPublic()){
            new Button("Make Private", this::makePrivate).addTo(panel);
        }else new Button("Make Public", this::makePublic).addTo(panel);

        new Button("Clear Instances", this::clearInstances).addTo(panel);
        new Button("Analyse").addTo(panel);
        new Button("Close", this::close).addTo(panel);
        return panel;
    }

    private void clearInstances(){
        form.deleteAllInstances();
        form.save();
        affichage(normal);
    }

    private void makePublic(){
        controller.makePublic();
        affichage(normal);

    }

    private void makePrivate(){
        form.setIsPublic(false);
        form.save();
        affichage(normal);
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

