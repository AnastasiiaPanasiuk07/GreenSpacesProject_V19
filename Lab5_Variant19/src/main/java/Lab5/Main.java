package Lab5;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("===== LAB 5: JAVA COLLECTIONS (VARIANT 19) =====");
        int numberOfStudents = 95;
        List<Student> students = StudentGenerator.generateStudents(numberOfStudents);

        System.out.println("\nGenerated students (first 5):");
        for (int i = 0; i < Math.min(5, students.size()); i++) {
            System.out.println(students.get(i));
        }

        System.out.println("\n--- Sorting by grade (Comparable) ---");
        Collections.sort(students);
        for (int i = 0; i < Math.min(5, students.size()); i++) {
            System.out.println(students.get(i));
        }

        System.out.println("\n--- Sorting by name (Comparator) ---");
        students.sort(new StudentComparator());
        for (int i = 0; i < Math.min(5, students.size()); i++) {
            System.out.println(students.get(i));
        }

        System.out.println("\n--- Testing HashSet ---");
        Set<Student> studentSet = new HashSet<>();
        studentSet.addAll(students);
        Student first = students.get(0);
        Student duplicate = new Student(first.getId(), "DuplicateName", first.getGrade());
        boolean duplicateAdded = studentSet.add(duplicate);
        System.out.println("Duplicate added (expected false): " + duplicateAdded);
        System.out.println("HashSet size = " + studentSet.size());

        System.out.println("\n--- Testing HashMap ---");
        Map<Integer, Student> studentMap = new HashMap<>();
        for (Student s : students) {
            studentMap.put(s.getId(), s);
        }
        System.out.println("HashMap size = " + studentMap.size());

        int searchId = 15;
        System.out.println("Searching student with id = " + searchId);
        Student found = studentMap.get(searchId);
        System.out.println("Result: " + found);

        System.out.println("\n--- Performance test HashMap ---");
        performanceTest(95);
        performanceTest(1000);
        performanceTest(10000);

        System.out.println("\n--- Collision experiment (BadStudent) ---");
        collisionTest(95);
        collisionTest(1000);
        collisionTest(10000);
    }

    private static void performanceTest(int size) {
        Map<Student, Student> map = new HashMap<>();
        List<Student> students = StudentGenerator.generateStudents(size);
        for (Student s : students) {
            map.put(s, s);
        }

        int warmup = 100_000;
        int repeats = 1_000_000;
        long checksum = 0;

        for (int i = 0; i < warmup; i++) {
            Student key = students.get(i % size);
            Student found = map.get(key);
            checksum += found.getId();
        }

        long start = System.nanoTime();
        for (int i = 0; i < repeats; i++) {
            Student key = students.get(i % size);
            Student found = map.get(key);
            checksum += found.getId();
        }
        long end = System.nanoTime();

        double averageTime = (end - start) / (double) repeats;
        System.out.println("Size = " + size + " | average search time = " + averageTime + " ns");
        if (checksum == 0) System.out.println();
    }

    private static void collisionTest(int size) {
        Map<BadStudent, BadStudent> map = new HashMap<>();
        List<BadStudent> students = StudentGenerator.generateBadStudents(size);
        for (BadStudent s : students) {
            map.put(s, s);
        }

        int warmup = 100_000;
        int repeats = 1_000_000;
        long checksum = 0;

        for (int i = 0; i < warmup; i++) {
            BadStudent key = students.get(i % size);
            BadStudent found = map.get(key);
            checksum += found.getId();
        }

        long start = System.nanoTime();
        for (int i = 0; i < repeats; i++) {
            BadStudent key = students.get(i % size);
            BadStudent found = map.get(key);
            checksum += found.getId();
        }
        long end = System.nanoTime();

        double averageTime = (end - start) / (double) repeats;
        System.out.println("Collision test size = " + size + " | average search time = " + averageTime + " ns");
        if (checksum == 0) System.out.println();
    }
}
