package tgpr.forms.controller;

import tgpr.forms.model.OptionList;
import tgpr.forms.model.Question;
import tgpr.forms.view.AddEditQuestionView;
import tgpr.framework.Controller;

import java.util.List;

public class AddEditQuestionController extends Controller<AddEditQuestionView> {
    private final Question question;

    public AddEditQuestionController(Question question) {
        this.question = question;  // Question à modifier ou null pour une nouvelle question
    }

    @Override
    public AddEditQuestionView getView() {
        return new AddEditQuestionView(this, question);
    }

    public void createQuestion(String title, String description, Question.Type type, OptionList optionList) {
        Question newQuestion = new Question();
        newQuestion.setTitle(title);
        newQuestion.setDescription(description);
        newQuestion.setType(type);
        newQuestion.setRequired(false); // Set default value or update according to your requirements

        // Handle optionList if required by the question type
        if (type.requiresOptionList()) {
            newQuestion.setOptionListId(optionList != null ? optionList.getId() : null);
        }

        // Save the new question in the database
        newQuestion.save();
        getView().close();
    }

    public void updateQuestion(Question question, String title, String description, Question.Type type, OptionList optionList) {
        // Update the existing question
        question.setTitle(title);
        question.setDescription(description);
        question.setType(type);

        // Handle optionList if required by the question type
        if (type.requiresOptionList()) {
            question.setOptionListId(optionList != null ? optionList.getId() : null);
        } else {
            question.setOptionListId(null); // Clear option list if not required
        }

        // Save the updated question in the database
        question.save();
        getView().close();
    }

    public void deleteQuestion(Question question) {
        if(askConfirmation("are u sure to delete question","Delete question"))
            question.delete();
        getView().close();
    }
    public List<OptionList> getOptionLists() {
        return OptionList.getAll();
    }

}

