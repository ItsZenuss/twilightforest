package twilightforest.port.neoforged.neoforge.common;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModConfigSpec {
	private final Map<String, ConfigValue<?>> values;

	public ModConfigSpec(Map<String, ConfigValue<?>> values) {
		this.values = values;
	}

	/**
	 * Fabric port: reads values from a flat JSON file ("section.key": value), then rewrites it so new options
	 * appear with their defaults. Unknown or malformed entries fall back to defaults.
	 */
	public void loadAndSave(java.nio.file.Path file) {
		com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
		com.google.gson.JsonObject json = new com.google.gson.JsonObject();
		if (java.nio.file.Files.exists(file)) {
			try (java.io.Reader reader = java.nio.file.Files.newBufferedReader(file)) {
				com.google.gson.JsonElement parsed = com.google.gson.JsonParser.parseReader(reader);
				if (parsed.isJsonObject()) json = parsed.getAsJsonObject();
			} catch (Exception e) {
				org.slf4j.LoggerFactory.getLogger("twilightforest-config").warn("Could not read {}, using defaults", file, e);
			}
		}
		com.google.gson.JsonObject out = new com.google.gson.JsonObject();
		for (ConfigValue<?> value : this.values.values()) {
			com.google.gson.JsonElement element = json.get(value.getPath());
			if (element != null) {
				try {
					value.readJson(gson, element);
				} catch (Exception e) {
					org.slf4j.LoggerFactory.getLogger("twilightforest-config").warn("Invalid value for {} in {}, using default", value.getPath(), file);
				}
			}
			out.add(value.getPath(), value.toJson(gson));
		}
		try {
			java.nio.file.Files.createDirectories(file.getParent());
			java.nio.file.Files.writeString(file, gson.toJson(out));
		} catch (java.io.IOException e) {
			org.slf4j.LoggerFactory.getLogger("twilightforest-config").warn("Could not write {}", file, e);
		}
	}

	public static class ConfigValue<T> implements Supplier<T> {
		private final String path;
		private final T defaultValue;
		private T value;

		public ConfigValue(String path, T defaultValue) {
			this.path = path;
			this.defaultValue = defaultValue;
			this.value = defaultValue;
		}

		@Override
		public T get() {
			return value != null ? value : defaultValue;
		}

		public void set(T value) {
			this.value = value;
		}

		public T getDefault() {
			return defaultValue;
		}

		public String getPath() {
			return path;
		}

		com.google.gson.JsonElement toJson(com.google.gson.Gson gson) {
			T current = this.get();
			if (current instanceof Enum<?> e) return new com.google.gson.JsonPrimitive(e.name());
			return gson.toJsonTree(current);
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		void readJson(com.google.gson.Gson gson, com.google.gson.JsonElement element) {
			Object def = this.defaultValue;
			Object parsed;
			if (def instanceof Enum<?> e) parsed = Enum.valueOf((Class) e.getDeclaringClass(), element.getAsString());
			else if (def instanceof Boolean) parsed = element.getAsBoolean();
			else if (def instanceof Integer) parsed = element.getAsInt();
			else if (def instanceof Long) parsed = element.getAsLong();
			else if (def instanceof Double) parsed = element.getAsDouble();
			else if (def instanceof String) parsed = element.getAsString();
			else if (def instanceof List<?>) {
				List<Object> list = new ArrayList<>();
				for (com.google.gson.JsonElement el : element.getAsJsonArray()) {
					list.add(el.isJsonPrimitive() && el.getAsJsonPrimitive().isNumber() ? el.getAsNumber() : el.isJsonPrimitive() ? el.getAsString() : el);
				}
				parsed = list;
			} else parsed = gson.fromJson(element, def == null ? Object.class : def.getClass());
			this.set((T) parsed);
		}
	}

	public static class BooleanValue extends ConfigValue<Boolean> {
		public BooleanValue(String path, Boolean defaultValue) {
			super(path, defaultValue);
		}
	}

	public static class IntValue extends ConfigValue<Integer> {
		private final int min;
		private final int max;

		public IntValue(String path, Integer defaultValue, int min, int max) {
			super(path, defaultValue);
			this.min = min;
			this.max = max;
		}
	}

	public static class DoubleValue extends ConfigValue<Double> {
		private final double min;
		private final double max;

		public DoubleValue(String path, Double defaultValue, double min, double max) {
			super(path, defaultValue);
			this.min = min;
			this.max = max;
		}
	}

	public static class EnumValue<T extends Enum<T>> extends ConfigValue<T> {
		public EnumValue(String path, T defaultValue) {
			super(path, defaultValue);
		}
	}

	public static class Builder {
		private final List<String> currentPath = new ArrayList<>();
		private final Map<String, ConfigValue<?>> values = new LinkedHashMap<>();

		public Builder comment(String... comment) {
			return this;
		}

		public Builder translation(String translation) {
			return this;
		}

		public Builder push(String path) {
			currentPath.add(path);
			return this;
		}

		public Builder pop() {
			if (!currentPath.isEmpty()) {
				currentPath.remove(currentPath.size() - 1);
			}
			return this;
		}

		public Builder pop(int count) {
			for (int i = 0; i < count && !currentPath.isEmpty(); i++) {
				currentPath.remove(currentPath.size() - 1);
			}
			return this;
		}

		private String buildPath(String name) {
			if (currentPath.isEmpty()) return name;
			return String.join(".", currentPath) + "." + name;
		}

		public BooleanValue define(String path, boolean defaultValue) {
			BooleanValue val = new BooleanValue(buildPath(path), defaultValue);
			values.put(val.getPath(), val);
			return val;
		}

		public <T> ConfigValue<T> define(String path, T defaultValue) {
			ConfigValue<T> val = new ConfigValue<>(buildPath(path), defaultValue);
			values.put(val.getPath(), val);
			return val;
		}

		public IntValue defineInRange(String path, int defaultValue, int min, int max) {
			IntValue val = new IntValue(buildPath(path), defaultValue, min, max);
			values.put(val.getPath(), val);
			return val;
		}

		public DoubleValue defineInRange(String path, double defaultValue, double min, double max) {
			DoubleValue val = new DoubleValue(buildPath(path), defaultValue, min, max);
			values.put(val.getPath(), val);
			return val;
		}

		public <T extends Enum<T>> EnumValue<T> defineEnum(String path, T defaultValue) {
			EnumValue<T> val = new EnumValue<>(buildPath(path), defaultValue);
			values.put(val.getPath(), val);
			return val;
		}

		public <T> ConfigValue<List<? extends T>> defineList(String path, List<? extends T> defaultList, Predicate<Object> elementValidator) {
			ConfigValue<List<? extends T>> val = new ConfigValue<>(buildPath(path), defaultList);
			values.put(val.getPath(), val);
			return val;
		}

		public Builder worldRestart() {
			return this;
		}

		public <T> ConfigValue<List<? extends T>> defineListAllowEmpty(String path, Supplier<List<? extends T>> defaultList, Predicate<Object> elementValidator) {
			ConfigValue<List<? extends T>> val = new ConfigValue<>(buildPath(path), defaultList.get());
			values.put(val.getPath(), val);
			return val;
		}

		public <T> ConfigValue<List<? extends T>> defineListAllowEmpty(String path, List<? extends T> defaultList, Supplier<T> newElementSupplier, Predicate<Object> elementValidator) {
			ConfigValue<List<? extends T>> val = new ConfigValue<>(buildPath(path), defaultList);
			values.put(val.getPath(), val);
			return val;
		}

		public <T> ConfigValue<List<? extends T>> defineListAllowEmpty(List<String> path, List<? extends T> defaultList, Supplier<T> newElementSupplier, Predicate<Object> elementValidator) {
			return defineListAllowEmpty(String.join(".", path), defaultList, newElementSupplier, elementValidator);
		}

		public <T> Pair<T, ModConfigSpec> configure(Function<Builder, T> consumer) {
			T config = consumer.apply(this);
			return Pair.of(config, new ModConfigSpec(values));
		}

		public ModConfigSpec build() {
			return new ModConfigSpec(values);
		}
	}
}
