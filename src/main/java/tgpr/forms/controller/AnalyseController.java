package tgpr.forms.controller;

import tgpr.forms.model.Answer;
import tgpr.forms.model.Form;
import tgpr.forms.model.OptionValue;
import tgpr.forms.model.Question;
import tgpr.forms.view.AnalyseView;
import tgpr.framework.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyseController extends Controller<AnalyseView> {
    private final AnalyseView view;
    private Form currentForm;


    public AnalyseController(Form currentFormForm) {
        this.currentForm = currentFormForm;
        this.view = new AnalyseView(this, currentForm);
    }

    public AnalyseView getView() {
        return view;
    }

    public int getSubmittedInstancesCount(){
        return currentForm.getCompletedInstances().size();
    }

    public Map<String, Long> getAnswerStatistics(Question question) {
        List<Answer> answers = question.getAnswers();
        return answers.stream()
                .collect(Collectors.groupingBy(answer -> {
                    String value = answer.getValue();
                    return (value == null || value.isBlank()) ? "--- vide ---" : value;
                }, Collectors.counting()));
    }

    // Méthode pour obtenir la valeur à afficher pour chaque réponse
    private String getDisplayValue(Answer answer, Question question) {
        try {
            int index = Integer.parseInt(answer.getValue());
            OptionValue optionValue = OptionValue.getByKey(index, question.getOptionListId());
            return optionValue != null ? optionValue.getLabel() : "Unknown value";
        } catch (NumberFormatException e) {
            return answer.getValue();
        }
    }

    //Obtenir le nombre total de réponses pour une question
    public long getTotalResponses(Question question) {
        return question.getAnswers().size();
    }
}
