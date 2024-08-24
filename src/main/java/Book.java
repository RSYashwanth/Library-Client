import java.time.LocalDate;

public class Book {
    private int id;
    private String name;
    private LocalDate date1;
    private LocalDate date2;

    public Book(int id, String name, LocalDate date1, LocalDate date2) {
        this.name = name;
        this.date1 = date1;
        this.date2 = date2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate1() {
        return date1;
    }

    public LocalDate getDate2() {
        return date2;
    }
}
