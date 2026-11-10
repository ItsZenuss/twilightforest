package twilightforest.port.neoforged.fml.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObfuscationReflectionHelper {
	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			try {
				Method method = current.getDeclaredMethod(methodName, parameterTypes);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException ignored) {
			}
			current = current.getSuperclass();
		}
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				method.setAccessible(true);
				return method;
			}
		}
		throw new RuntimeException("Unable to find method " + methodName + " in class " + clazz.getName());
	}

	public static Field findField(Class<?> clazz, String fieldName) {
		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			try {
				Field field = current.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException ignored) {
			}
			current = current.getSuperclass();
		}
		throw new RuntimeException("Unable to find field " + fieldName + " in class " + clazz.getName());
	}
}
