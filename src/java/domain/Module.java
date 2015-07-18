package domain;

import java.util.Calendar;

/**
 * Represents a module from the database
 * @author r0430844
 */
public class Module {
    private String name, chapter;
    private int maxTries;
    private Calendar start, deadline;
    private long id;

    public Module(){}
    
    public Module(long id, String name, String chapter, int maxTries, Calendar deadline, Calendar start) {
        this.name = name.trim();
        this.chapter = chapter.trim();
        this.maxTries = maxTries;
        this.start = start;
        this.deadline = deadline;
        this.id = id;
    }
    
    public Module(String name, String chapter, int maxTries, Calendar deadline, Calendar start) {
        this.name = name.trim();
        this.chapter = chapter.trim();
        this.maxTries = maxTries;
        this.start = start;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter.trim();
    }

    public int getMaxTries() {
        return maxTries;
    }

    public void setMaxTries(int maxTries) {
        this.maxTries = maxTries;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public CalendarWrapper getStartWrapper(){
        return new CalendarWrapper(start);
    }
    
    public CalendarWrapper getDeadlineWrapper(){
        return new CalendarWrapper(deadline);
    }
    
    public boolean isAvailable(){
        Calendar today = Calendar.getInstance();
        return (today.before(deadline) && today.after(start));
    }
    
}
