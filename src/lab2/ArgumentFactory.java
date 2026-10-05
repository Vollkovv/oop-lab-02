package lab2;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Создаёт значения аргументов по типам параметров метода, не передавая null.
 */
public class ArgumentFactory {

    private static final int MAX_DEPTH = 5;

    private int stringCounter;

    public Object[] argumentsFor(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(type -> create(type, 0))
                .toArray();
    }

    public Object create(Class<?> type, int depth) {
        if (depth > MAX_DEPTH) {
            throw new IllegalArgumentException("Слишком глубокая вложенность при создании " + type.getName());
        }

        if (type == int.class || type == Integer.class) return 3;
        if (type == long.class || type == Long.class) return 42L;
        if (type == double.class || type == Double.class) return 1.5;
        if (type == float.class || type == Float.class) return 2.5f;
        if (type == boolean.class || type == Boolean.class) return true;
        if (type == char.class || type == Character.class) return '*';
        if (type == byte.class || type == Byte.class) return (byte) 1;
        if (type == short.class || type == Short.class) return (short) 1;

        if (type == Object.class) return new Object();
        if (type == String.class) return "слово" + (++stringCounter);
        if (type.isArray()) return Array.newInstance(type.getComponentType(), 0);
        if (type.isEnum()) return type.getEnumConstants()[0];
        if (type.isAssignableFrom(ArrayList.class) || List.class == type || Collection.class == type) {
            return new ArrayList<>(List.of("a", "b", "c"));
        }
        if (type.isAssignableFrom(HashSet.class) || Set.class == type) return new HashSet<>();
        if (type.isAssignableFrom(HashMap.class) || Map.class == type) return new HashMap<>();

        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            throw new IllegalArgumentException("Нельзя создать объект интерфейса или абстрактного класса " + type.getName());
        }
        return createByConstructor(type, depth);
    }

    private Object createByConstructor(Class<?> type, int depth) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new IllegalArgumentException("У класса " + type.getName() + " нет конструкторов");
        }

        Constructor<?> constructor = Arrays.stream(constructors)
                .filter(c -> c.getParameterCount() == 0)
                .findFirst()
                .orElseGet(() -> Arrays.stream(constructors)
                        .min(Comparator.comparingInt(Constructor::getParameterCount))
                        .orElseThrow());

        Object[] args = Arrays.stream(constructor.getParameterTypes())
                .map(paramType -> create(paramType, depth + 1))
                .toArray();

        constructor.setAccessible(true);
        try {
            return constructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Конструктор " + type.getName() + " завершился с ошибкой", e);
        }
    }
}
