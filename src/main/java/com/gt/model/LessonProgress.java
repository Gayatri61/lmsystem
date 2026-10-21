package com.gt.model;

public class LessonProgress {

    private Lesson lesson;
    private boolean completed;

    public LessonProgress(Lesson lesson) {
        this.lesson = lesson;
        this.completed = false;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}