package lab2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Вызывает у объекта все protected и private методы, помеченные {@link Repeat},
 * столько раз, сколько указано в аннотации.
 */
public class AnnotatedMethodInvoker {

    private final ArgumentFactory argumentFactory = new ArgumentFactory();

    public void invokeAnnotated(Object target) {
        Method[] methods = target.getClass().getDeclaredMethods();
        // getDeclaredMethods не гарантирует порядок — сортируем, чтобы вывод был стабильным
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        for (Method method : methods) {
            Repeat repeat = method.getAnnotation(Repeat.class);
            if (repeat == null || !isProtectedOrPrivate(method)) {
                continue;
            }
            if (repeat.value() < 0) {
                System.out.println(method.getName() + ": отрицательное число повторов, пропускаю");
                continue;
            }

            Object[] args;
            try {
                args = argumentFactory.argumentsFor(method);
            } catch (IllegalArgumentException e) {
                System.out.println(method.getName() + ": не удалось подготовить аргументы — " + e.getMessage());
                continue;
            }
            method.setAccessible(true); // доступ к private и protected из другого класса

            System.out.printf("%s %s%s — вызов %d раз(а)%n",
                    Modifier.toString(method.getModifiers()),
                    method.getName(),
                    Arrays.toString(args),
                    repeat.value());

            for (int i = 0; i < repeat.value(); i++) {
                invoke(target, method, args);
            }
        }
    }

    private static boolean isProtectedOrPrivate(Method method) {
        int modifiers = method.getModifiers();
        return Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers);
    }

    private static void invoke(Object target, Method method, Object[] args) {
        try {
            method.invoke(target, args);
        } catch (InvocationTargetException e) {
            // Исключение, выброшенное самим вызванным методом
            System.out.println("  метод " + method.getName() + " выбросил исключение: " + e.getCause());
        } catch (IllegalAccessException e) {
            System.out.println("  нет доступа к методу " + method.getName() + ": " + e.getMessage());
        }
    }
}
