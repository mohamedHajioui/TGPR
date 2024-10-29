package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.model.OptionList;
import tgpr.forms.model.Question;
import tgpr.forms.view.AddEditQuestionView;
import tgpr.framework.Controller;

import java.util.List;

public class AddEditQuestionController extends Controller<AddEditQuestionView> {
    private final Question question;
    private final Form form;
    private int formId;

    public AddEditQuestionController(Question question,Form form) {
        this.question = question; // Question à modifier ou null pour une nouvelle question
        this.form = form;
        this.formId = form.getId();
    }

    @Override
    public AddEditQuestionView getView() {
        return new AddEditQuestionView(this, question);
    }

    public int getNextIdxForForm() {
        int lastIdx = form.getQuestions().size();
        return lastIdx + 1;
    }
    public void createQuestion(String title, String description, Question.Type type, OptionList optionList, boolean required) {

        Question newQuestion = new Question();
        newQuestion.setTitle(title);
        newQuestion.setDescription(description);
        newQuestion.setType(type);
        newQuestion.setRequired(required);
        newQuestion.setFormId(formId);
        newQuestion.setIdx(getNextIdxForForm());

        // Handle optionList if required by the question type
        if (type.requiresOptionList()) {
            newQuestion.setOptionListId(optionList != null ? optionList.getId() : null);
        }

        // Save the new question in the database
        newQuestion.save();
        getView().close();
    }

    public void updateQuestion(Question question, String title, String description, Question.Type type, OptionList optionList, boolean required) {
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
        question.setRequired(required);

        // Save the updated question in the database
        question.save();
        getView().close();
    }

    public void deleteQuestion(Question question) {
        if(askConfirmation("are you sure you want to delete question","Delete question"))
            question.delete();
        getView().close();
    }
    public List<OptionList> getOptionLists() {
        return OptionList.getAll();
    }

}

