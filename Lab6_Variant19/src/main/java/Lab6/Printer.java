package Lab6;

@FunctionalInterface
public interface Printer<T> {
    void print(T obj);
}