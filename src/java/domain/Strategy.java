package domain;

/**
 * Represents the strategy
 * @author r0430844
 */
public class Strategy {
    private String name;
    private long id;

    public Strategy() {
    }

    public Strategy(long id,String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    
    
}
