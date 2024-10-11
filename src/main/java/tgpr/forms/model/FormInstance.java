package tgpr.forms.model;

// Model/FormInstance.java
import java.time.LocalDateTime;
import java.util.List;

public class FormInstance {
    private int id;
    private int userId;
    private int formId;
    private LocalDateTime started;
    private LocalDateTime completed;
    private List<Question> questions;

    public FormInstance(int id, int userId, int formId, LocalDateTime started, List<Question> questions) {
        this.id = id;
        this.userId = userId;
        this.formId = formId;
        this.started = started;
        this.completed = null; // Pas encore complétée
        this.questions = questions;
    }

    public boolean isSubmitted() {
        return completed != null;
    }

    public void setCompleted(LocalDateTime completed) {
        this.completed = completed;
    }

    // Getters et setters
}

