package tgpr.forms.view;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableRenderer;
import org.slf4j.helpers.LegacyAbstractLogger;
import tgpr.forms.controller.AnalyseController;
import tgpr.forms.model.Form;
import tgpr.forms.model.Question;

import java.util.List;
import java.util.Map;

public class AnalyseView extends DialogWindow {
    private final AnalyseController controller;
    private Panel mainPanel;
    private Panel tablesPanel;
    private Table<String> answersTable;

    public AnalyseView(AnalyseController controller, Form currentForm) {
        super("Statistical Analysis of Submitted Instances");
        this.controller = controller;

        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(90, 23));
        setHints(List.of(Hint.CENTERED));
        setComponent(mainPanel);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Panel titlePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label titleLabel = new Label("Title:                        ");
        Label titleForm = new Label(currentForm.getTitle());
        titlePanel.addComponent(titleLabel);
        titlePanel.addComponent(titleForm);
        mainPanel.addComponent(titlePanel);


        Panel descriptionPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label description = new Label("Description:                  ");
        descriptionPanel.addComponent(description);
        Label descriptionForm = new Label(currentForm.getDescription());
        descriptionPanel.addComponent(descriptionForm);
        mainPanel.addComponent(descriptionPanel);
        Panel instancesPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label nbInstances = new Label("Number of Submitted Instances: " + controller.getSubmittedInstancesCount());
        mainPanel.addComponent(nbInstances);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        tablesPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        displayQuestionsTable(currentForm.getQuestions());
        displayAnswersTable();
        mainPanel.addComponent(tablesPanel);


    }

    public void displayQuestionsTable(List<Question> questions){
        Table<String> questionsTable = new Table<>("Index", "Title                         ");
        questionsTable.setPreferredSize(new TerminalSize(60, 10));
        //remplissage du tableau avec les questions
        for (Question question : questions) {
            questionsTable.getTableModel().addRow(
                    String.valueOf("    " + question.getIdx()),
                    question.getTitle()
            );
        }
        //configurer la selections
        questionsTable.setSelectAction(() -> {
            //action lors de la sélection d'une question
            int selectedRow = questionsTable.getSelectedRow();
            Question selectedQuestion = questions.get(selectedRow);
            System.out.println("Selected question: " + selectedQuestion.getTitle());
            updateAnswersTable(selectedQuestion);
            updateAnswersTable(selectedQuestion);
        });
        tablesPanel.addComponent(questionsTable);
    }

    public void displayAnswersTable(){


        answersTable = new Table<>("Value                ", "Nb Occ", "   Ratio");
        answersTable.setPreferredSize(new TerminalSize(50, 10));
        tablesPanel.addComponent(answersTable);
    }

    public void updateAnswersTable(Question question) {
        answersTable.getTableModel().clear();
        Map<String, Long> answerStats = controller.getAnswerStatistics(question);
        long totalResponses = controller.getTotalResponses(question);

        // Tri des réponses en fonction du nombre d'occurrences (du plus grand au plus petit)
        answerStats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    String answerText = entry.getKey();
                    Long occurrences = entry.getValue();
                    double ratio = totalResponses > 0 ? (double) occurrences / totalResponses * 100 : 0;
                    answersTable.getTableModel().addRow(
                            answerText,
                            "     " + occurrences.toString(),
                            String.format("%.2f", ratio)
                    );
                });
    }
}