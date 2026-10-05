package lab2;


public class Main {

    public static void main(String[] args) {
        Worker worker = new Worker();
        new AnnotatedMethodInvoker().invokeAnnotated(worker);
    }
}
