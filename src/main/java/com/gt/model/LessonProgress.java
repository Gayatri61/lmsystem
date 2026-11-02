package com.gt.model;

public class LessonProgress {

    private Enrollment enrollment;
    private Lesson lesson;  
    private boolean completed;

    public LessonProgress() {
    }

    public LessonProgress( Enrollment enrollment, Lesson lesson, boolean completed) {
        this.enrollment = enrollment;
        this.lesson = lesson;
        this.completed = completed;
    }


    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
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