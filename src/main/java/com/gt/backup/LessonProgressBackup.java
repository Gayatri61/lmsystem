package com.gt.backup;

/**
 * LessonProgressBackup
 */
public class LessonProgressBackup {
    
    private int enrollmentId;
    private int lessonId;
    private boolean completed;

    public LessonProgressBackup() {
    }

    public LessonProgressBackup(int enrollmentId, int lessonId, boolean completed) {
        this.enrollmentId = enrollmentId;
        this.lessonId = lessonId;
        this.completed = completed;
    }


    public int getEnrollmentId() {
        return enrollmentId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public boolean isCompleted() {
        return completed;
    }

}
