package Lab5;

public class BadStudent implements Comparable<BadStudent> {
    private final int id;
    private final String name;
    private final int grade;

    public BadStudent(int id, String name, int grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BadStudent)) return false;
        BadStudent other = (BadStudent) o;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public int compareTo(BadStudent other) {
        return Integer.compare(this.id, other.id);
    }
}
