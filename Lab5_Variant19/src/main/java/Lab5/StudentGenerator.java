package Lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentGenerator {
    private static final String[] NAMES = {
            "Ivan", "Petro", "Olena", "Anna", "Mykola",
            "Andrii", "Oksana", "Dmytro", "Iryna", "Serhii"
    };
    private static Random random = new Random();

    public static List<Student> generateStudents(int count) {
        List<Student> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String name = NAMES[random.nextInt(NAMES.length)];
            int grade = 60 + random.nextInt(41); 
            list.add(new Student(i, name, grade));
        }
        return list;
    }

    public static List<BadStudent> generateBadStudents(int count) {
        List<BadStudent> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String name = NAMES[random.nextInt(NAMES.length)];
            int grade = 60 + random.nextInt(41);
            list.add(new BadStudent(i, name, grade));
        }
        return list;
    }
}
