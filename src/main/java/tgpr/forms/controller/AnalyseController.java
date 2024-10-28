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
                .map(answer -> {
                    String value = answer.getValue();
                    if (isNumeric(value)) {
                        // Si la value est numérique, récupérer le label de l'OptionValue correspondant
                        OptionValue optionValue = OptionValue.getByKey(Integer.parseInt(value), question.getOptionListId());
                        return optionValue != null ? optionValue.getLabel() : "--- vide ---";
                    } else {
                        // Si ce n'est pas un identifiant, utiliser la valeur elle-même
                        return value != null && !value.isEmpty() ? value : "--- vide ---";
                    }
                })
                .collect(Collectors.groupingBy(label -> label, Collectors.counting()));
    }

    // Méthode utilitaire pour vérifier si une chaîne est numérique
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    //Obtenir le nombre total de réponses pour une question
    public long getTotalResponses(Question question) {
        return question.getAnswers().size();
    }
}
