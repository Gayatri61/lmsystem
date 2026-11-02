package com.gt.backup;



import java.util.List;


public class BackupData {

    private List<UserBackup> users;
    private List<StudentBackup> students;
    private List<InstructorBackup> instructors;
    private List<CourseBackup> courses;
    private List<LessonBackup> lessons;
    private List<EnrollmentBackup> enrollments;
    private List<LessonProgressBackup> lessonProgress;

    public BackupData() {
    }

    public BackupData(List<UserBackup> userBackups, List<StudentBackup> studentBackups,List<InstructorBackup> instructorsBackups,
            List<CourseBackup> courseBackups, List<LessonBackup> lessonsBackups,
            List<EnrollmentBackup> enrollmentsBackups, List<LessonProgressBackup> lessonProgressBackups) {
        this.users=userBackups;
        this.students=studentBackups;
        this.instructors=instructorsBackups;
        this.courses=courseBackups;
        this.lessons=lessonsBackups;
        this.enrollments=enrollmentsBackups;
        this.lessonProgress=lessonProgressBackups;

    }

    public List<UserBackup> getUsers() {
        return users;
    }

    public void setUsers(List<UserBackup> users) {
        this.users = users;
    }

    public List<CourseBackup> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseBackup> courses) {
        this.courses = courses;
    }

    public List<LessonBackup> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonBackup> lessons) {
        this.lessons = lessons;
    }

    public List<EnrollmentBackup> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<EnrollmentBackup> enrollments) {
        this.enrollments = enrollments;
    }

    public List<LessonProgressBackup> getLessonProgress() {
        return lessonProgress;
    }

    public void setLessonProgress(List<LessonProgressBackup> lessonProgress) {
        this.lessonProgress = lessonProgress;
    }

    public List<StudentBackup> getStudents() {
        return students;
    }

    public void setStudents(List<StudentBackup> students) {
        this.students = students;
    }

    public List<InstructorBackup> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<InstructorBackup> instructors) {
        this.instructors = instructors;
    }

    


}
