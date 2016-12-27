package tc.oc.minecraft.api.configuration;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;

import tc.oc.time.Durations;

public abstract class AbstractConfigurationSection implements ConfigurationSection {

    @Override
    public String resolvePath(String key) {
        final String base = getCurrentPath();
        return base.length() == 0 ? key : base + "." + key;
    }

    protected int indexOfNonInstance(List<?> list, Class<?> type) {
        for(int i = 0; i < list.size(); i++) {
            if(!type.isInstance(list.get(i))) return i;
        }
        return -1;
    }

    @Override
    public <T> T getType(String path, Class<T> type) {
        final Object value = get(path);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    @Override
    public @Nullable <T> T getParsed(String path, Function<? super String, ? extends T> parser) throws InvalidConfigurationException {
        final Object value = get(path);
        if(value == null) return null;
        final String text = value.toString();
        try {
            return parser.apply(text);
        } catch(RuntimeException e) {
            throw new InvalidConfigurationException(this, path, "Failed to parse '" + text + "': " + e.toString());
        }
    }

    @Override
    public <T> T getParsed(String path, T def, Function<? super String, ? extends T> parser) throws InvalidConfigurationException {
        final T value = getParsed(path, parser);
        return value != null ? value : def;
    }

    @Override
    public @Nullable Duration getDuration(String path) throws InvalidConfigurationException {
        return getParsed(path, Durations::parse);
    }

    @Override
    public Duration getDuration(String path, Duration def) throws InvalidConfigurationException {
        return getParsed(path, def, Duration::parse);
    }

    @Override
    public <T> List<T> getListOf(String path, Class<T> type) {
        final List<?> list = getList(path);
        if(list == null) return null;

        final int i = indexOfNonInstance(list, type);
        if(i >= 0) return null;

        return (List<T>) list;
    }

    @Override
    public <T> T needType(String path, Class<T> type) throws InvalidConfigurationException {
        final Object value = get(path);
        if(value == null) {
            throw new InvalidConfigurationException(this, path, "Missing required " + (Object.class.equals(type) ? "value" : type.getSimpleName()));
        }
        if(!type.isInstance(value)) {
            throw new InvalidConfigurationException(this, path, "Expected a " + type.getSimpleName() + " rather than a " + value.getClass().getSimpleName());
        }
        return type.cast(value);
    }

    @Override
    public Object need(String path) throws InvalidConfigurationException {
        return needType(path, Object.class);
    }

    @Override
    public ConfigurationSection needSection(String path) throws InvalidConfigurationException {
        return needType(path, ConfigurationSection.class);
    }

    @Override
    public int needInt(String path) throws InvalidConfigurationException {
        return needType(path, Number.class).intValue();
    }

    @Override
    public long needLong(String path) throws InvalidConfigurationException {
        return needType(path, Number.class).longValue();
    }

    @Override
    public double needDouble(String path) throws InvalidConfigurationException {
        return needType(path, Number.class).doubleValue();
    }

    @Override
    public boolean needBoolean(String path) throws InvalidConfigurationException {
        return needType(path, Boolean.class);
    }

    @Override
    public String needString(String path) throws InvalidConfigurationException {
        return needType(path, String.class);
    }

    @Override
    public <T> T needParsed(String path, Function<? super String, ? extends T> parser) throws InvalidConfigurationException {
        final T value = getParsed(path, parser);
        if(value == null) {
            throw new InvalidConfigurationException(this, path, "Missing required value");
        }
        return value;
    }

    @Override
    public Duration needDuration(String path) throws InvalidConfigurationException {
        return needParsed(path, Duration::parse);
    }

    @Override
    public List<?> needList(String path) throws InvalidConfigurationException {
        return needType(path, List.class);
    }

    @Override
    public <T> List<T> needList(String path, Class<T> type) throws InvalidConfigurationException {
        final List<?> list = needList(path);
        final int i = indexOfNonInstance(list, type);
        if(i >= 0) {
            throw new InvalidConfigurationException(this, path, i, "Expected a " + type.getSimpleName() + " rather than a " + list.get(i).getClass().getSimpleName());
        }
        return (List<T>) list;
    }
}
