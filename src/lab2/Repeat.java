package lab2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает метод, который нужно вызвать указанное число раз.
 * RUNTIME — чтобы аннотация была видна через рефлексию во время работы программы.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Repeat {

    /** Сколько раз вызвать метод. */
    int value();
}
