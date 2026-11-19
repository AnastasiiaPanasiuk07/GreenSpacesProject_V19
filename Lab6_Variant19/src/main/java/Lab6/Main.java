package Lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        List<Device> devices = new ArrayList<>();
        devices.add(new Device(1, "Router", 30));
        devices.add(new Device(2, "Laptop", 65));
        devices.add(new Device(3, "Lamp", 20));
        devices.add(new Device(4, "Heater", 1500));
        devices.add(new Device(5, "Monitor", 40));

        System.out.println("===== LR6: Lambda (Variant 19) =====");

        System.out.println("\nInitial list:");
        devices.forEach(System.out::println);

        System.out.println("\n1) Named implementation:");
        Printer<Device> namedPrinter = new DevicePrinter();
        namedPrinter.print(devices.get(0));

        System.out.println("\n2) Anonymous class:");
        Printer<Device> anonymousPrinter = new Printer<Device>() {
            @Override
            public void print(Device obj) {
                System.out.println(obj);
            }
        };
        anonymousPrinter.print(devices.get(1));

        System.out.println("\n3) Lambda:");
        Printer<Device> lambdaPrinter = obj -> System.out.println(obj);
        lambdaPrinter.print(devices.get(2));

        System.out.println("\n4) Standard functional interfaces:");
        Predicate<Device> keepDevice = d -> d.getPower() >= 50;
        Function<Device, Integer> getPower = d -> d.getPower();
        Consumer<Device> consumer = d -> System.out.println(d);
        Supplier<Device> defaultDevice = () -> new Device(0, "DefaultDevice", 0);

        System.out.println("Predicate for first device (power >= 50): " + keepDevice.test(devices.get(0)));
        System.out.println("Function for first device (power): " + getPower.apply(devices.get(0)));
        consumer.accept(devices.get(0));
        System.out.println("Supplier: " + defaultDevice.get());

        System.out.println("\n5) forEach:");
        devices.forEach(d -> System.out.println(d));

        System.out.println("\n6) sort by power:");
        devices.sort((a, b) -> Integer.compare(a.getPower(), b.getPower()));
        devices.forEach(System.out::println);

        System.out.println("\n7) removeIf power < 50:");
        devices.removeIf(d -> d.getPower() < 50);
        devices.forEach(System.out::println);

        System.out.println("\n8) Method references:");
        devices.forEach(System.out::println);
        Supplier<ArrayList<Device>> factory = ArrayList::new;
        ArrayList<Device> emptyList = factory.get();
        System.out.println("Factory list size = " + emptyList.size());

        // Приклади типових compile-time помилок (закоментовані):
        // var f = x -> x + 1; // Помилка: немає цільового функціонального інтерфейсу[cite: 46]
        // Predicate<Integer> wrong = x -> x + 1; // Помилка: очікується boolean, а повертається int[cite: 47]
        // Printer<Device> wrongPrinter = (a, b) -> System.out.println(a); // Помилка: невідповідність кількості параметрів[cite: 47]

        System.out.println("\n===== Program finished =====");
    }
}
