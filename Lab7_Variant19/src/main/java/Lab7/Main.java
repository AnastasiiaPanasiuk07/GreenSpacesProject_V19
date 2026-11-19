package Lab7;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount(1, "Ivanenko Ivan", 15000));
        accounts.add(new BankAccount(2, "Petrenko Petro", -500));
        accounts.add(new BankAccount(3, "Kovalchuk Olena", 32000));
        accounts.add(new BankAccount(4, "Shevchenko Anna", 0));
        accounts.add(new BankAccount(5, "Melnyk Dmytro", 4500));
        accounts.add(new BankAccount(6, "Boyko Oksana", -1200));
        accounts.add(new BankAccount(7, "Kryvenko Serhii", 125000));

        System.out.println("=== Початковий список банківських рахунків ===");
        accounts.forEach(System.out::println);

        System.out.println("\n=== Фільтрація: balance > 0 ===");
        accounts.stream()
                .filter(a -> a.getBalance() > 0)
                .forEach(System.out::println);

        System.out.println("\n=== Map: список власників рахунків ===");
        List<String> owners = accounts.stream()
                .map(BankAccount::getOwner)
                .toList();
        owners.forEach(System.out::println);

        System.out.println("\n=== Сортування за балансом (зростання) ===");
        accounts.stream()
                .sorted((a1, a2) -> Integer.compare(a1.getBalance(), a2.getBalance()))
                .forEach(System.out::println);

        System.out.println("\n=== Агрегація: сума балансу (Stream) ===");
        int totalBalanceStream = accounts.stream()
                .mapToInt(BankAccount::getBalance)
                .sum();
        System.out.println("Сума балансу (Stream) = " + totalBalanceStream);

        System.out.println("\n=== Агрегація: сума балансу (for) ===");
        int totalBalanceFor = 0;
        for (BankAccount a : accounts) {
            totalBalanceFor += a.getBalance();
        }
        System.out.println("Сума балансу (for) = " + totalBalanceFor);

        System.out.println("\n=== Логічна перевірка: чи є рахунок з балансом < -1000 ===");
        boolean hasLargeDebt = accounts.stream()
                .anyMatch(a -> a.getBalance() < -1000);
        System.out.println("Результат: " + hasLargeDebt);

        runErrorFixDemonstration(accounts);
    }

    private static void runErrorFixDemonstration(List<BankAccount> accounts) {
        System.out.println("\n=== Демонстрація виправлення помилок (Варіант 19) ===");

        Stream<BankAccount> stream = accounts.stream();
        long count = stream.filter(a -> a.getBalance() > 0).count();
        System.out.println("Кількість додатних рахунків (виправлено): " + count);

        int totalBalance = accounts.stream()
                .mapToInt(BankAccount::getBalance)
                .sum();
        System.out.println("Загальний баланс через mapToInt.sum() (виправлено): " + totalBalance);

        List<BankAccount> filteredAccounts = accounts.stream()
                .filter(a -> a.getBalance() > 0)
                .toList();
        System.out.println("Розмір відфільтрованої колекції (виправлено): " + filteredAccounts.size());
    }
}
