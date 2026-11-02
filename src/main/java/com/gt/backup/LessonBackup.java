package com.gt.backup;

/**
 * LessonBackup
 */
public class LessonBackup {

    private int id;
    private String title;
    private String content;
    private int courseId;

    public LessonBackup() {
    }

    public LessonBackup(int id, String title, String content, int courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getCourseId() {
        return courseId;
    }
}
