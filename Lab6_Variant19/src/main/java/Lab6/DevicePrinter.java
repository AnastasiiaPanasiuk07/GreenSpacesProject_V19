package Lab6;

public class DevicePrinter implements Printer<Device> {
    @Override
    public void print(Device obj) {
        System.out.println(obj);
    }
}