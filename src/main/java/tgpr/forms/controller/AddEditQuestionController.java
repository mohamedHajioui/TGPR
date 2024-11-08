package tgpr.forms.controller;

import tgpr.forms.model.Form;
import tgpr.forms.model.OptionList;
import tgpr.forms.model.Question;
import tgpr.forms.model.User;
import tgpr.forms.view.AddEditQuestionView;
import tgpr.framework.Controller;

import java.util.List;

public class AddEditQuestionController extends Controller<AddEditQuestionView> {
    private final Question question;
    private final Form form;
    private int formId;
    private final User user;

    public AddEditQuestionController(Question question,Form form,User user) {
        this.question = question; // Question à modifier ou null pour une nouvelle question
        this.form = form;
        this.formId = form.getId();
        this.user = user;
    }

    @Override
    public AddEditQuestionView getView() {
        return new AddEditQuestionView(this, question,form);
    }

    public int getNextIdxForForm() {
        int lastIdx = form.getQuestions().size();
        return lastIdx + 1;
    }
    public boolean sameTitle(String title, Form form) {
        for (Question existingQuestion : form.getQuestions()) {
            if (existingQuestion.getTitle().equalsIgnoreCase(title)) {
                System.out.println("Titre en doublon");
                return true;
            }
        }
        return false;
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
        navigateTo(new formController(form,user));
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
        navigateTo(new formController(form,user));
    }

    public void deleteQuestion(Question question) {
        if(askConfirmation("are you sure you want to delete question","Delete question"))
            question.delete();
        navigateTo(new formController(form,user));
    }
    public List<OptionList> getOptionLists() {
        return OptionList.getAll();
    }

}

