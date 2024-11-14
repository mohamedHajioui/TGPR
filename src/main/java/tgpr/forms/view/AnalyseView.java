package tgpr.forms.view;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.table.DefaultTableRenderer;
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
    private Table<String> questionsTable;



    public AnalyseView(AnalyseController controller, Form currentForm) {
        super("Statistical Analysis of Submitted Instances");
        this.controller = controller;

        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(100, 25));
        setHints(List.of(Hint.CENTERED));
        setComponent(mainPanel);

        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        titlePanel(currentForm);
        descriptionPanel(currentForm);
        nbInstancesPanel(controller);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        tablesPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        tablesPanel.setPreferredSize(new TerminalSize(100, 20));
        displayQuestionsTable(currentForm.getQuestions());
        displayAnswersTable();
        mainPanel.addComponent(tablesPanel);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 7)));

        buttonsCloseAndViewInstance(controller);
    }

    private void nbInstancesPanel(AnalyseController controller) {
        Panel instancesPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label instancesLabel = new Label("Number of Submitted Instances:");
        Label nbInstances = new Label("" + controller.getSubmittedInstancesCount());
        nbInstances.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        instancesPanel.addComponent(instancesLabel);
        instancesPanel.addComponent(nbInstances);
        mainPanel.addComponent(instancesPanel);
    }

    private void descriptionPanel(Form currentForm) {
        Panel descriptionPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label description = new Label("Description:                  ");
        descriptionPanel.addComponent(description);
        Label descriptionForm = new Label(currentForm.getDescription());
        descriptionForm.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        descriptionPanel.addComponent(descriptionForm);
        mainPanel.addComponent(descriptionPanel);
    }

    private void titlePanel(Form currentForm) {
        Panel titlePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label titleLabel = new Label("Title:                        ");
        Label titleForm = new Label(currentForm.getTitle());
        titleForm.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        titlePanel.addComponent(titleLabel);
        titlePanel.addComponent(titleForm);
        mainPanel.addComponent(titlePanel);
    }

    private void buttonsCloseAndViewInstance(AnalyseController controller) {
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Button closeButton = new Button("Close", this::close);
        Button viewInstanceButton = new Button("View Instance", () -> controller.viewInstance());
        buttonsPanel.addComponent(closeButton);
        buttonsPanel.addComponent(viewInstanceButton);
        buttonsPanel.center();
        mainPanel.addComponent(buttonsPanel);
    }


    public void displayQuestionsTable(List<Question> questions){
        questionsTable = new Table<>("Index", "Title                         ");
        questionsTable.setPreferredSize(new TerminalSize(60, 10));

        for (Question question : questions) {
            questionsTable.getTableModel().addRow(
                    String.valueOf("    " + question.getIdx()),
                    question.getTitle()
            );
        }

        questionsTable.setRenderer(new DefaultTableRenderer<String>() {
            @Override
            public void drawComponent(TextGUIGraphics graphics, Table<String> component) {
                super.drawComponent(graphics, component);
                int selectedRow = questionsTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < questions.size()) {
                    Question selectedQuestion = questions.get(selectedRow);
                    updateAnswersTable(selectedQuestion);
                }
            }
        });
        tablesPanel.addComponent(questionsTable);
    }

    public void displayAnswersTable(){
        answersTable = new Table<>("Value                ", "Nb Occ.", "   Ratio");
        answersTable.setPreferredSize(new TerminalSize(50, 20));
        answersTable.setEnabled(false);
        tablesPanel.addComponent(answersTable);
    }

    public void updateAnswersTable(Question question) {
        answersTable.getTableModel().clear();

        Map<String, Long> answerStats = controller.getAnswerStatistics(question);
        long totalInstances = controller.getSubmittedInstancesCount();

        answerStats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    String answerText = entry.getKey();
                    Long occurrences = entry.getValue();
                    double ratio = totalInstances > 0 ? (double) occurrences / totalInstances * 100 : 0.0;

                    answersTable.getTableModel().addRow(
                            String.format("%-20s", answerText),
                            String.format("%7d", occurrences),
                            String.format("   %.1f%%", ratio)
                    );
                });
    }
}