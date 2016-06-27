package tc.oc.minecraft.api.configuration;

import java.util.Collection;
import java.util.List;

public interface ConfigurationSection {

    String getCurrentPath();

    String resolvePath(String key);

    ConfigurationSection getSection(String path);

    Collection<String> getKeys();

    <T> T getType(String path, Class<T> type);

    <T> T get(String path, T def);

    Object get(String path);

    void set(String path, Object value);

    int getInt(String path);

    int getInt(String path, int def);

    long getLong(String path);

    long getLong(String path, long def);

    double getDouble(String path);

    double getDouble(String path, double def);

    boolean getBoolean(String path);

    boolean getBoolean(String path, boolean def);

    String getString(String path);

    String getString(String path, String def);

    <T> List<T> getListOf(String path, Class<T> type);

    List<?> getList(String path);

    List<?> getList(String path, List<?> def);

    List<String> getStringList(String path);

    List<Byte> getByteList(String path);

    List<Short> getShortList(String path);

    List<Long> getLongList(String path);

    List<Float> getFloatList(String path);

    List<Double> getDoubleList(String path);

    List<Boolean> getBooleanList(String path);

    <T> T needType(String path, Class<T> type) throws InvalidConfigurationException;

    Object need(String path) throws InvalidConfigurationException;

    ConfigurationSection needSection(String path) throws InvalidConfigurationException;

    int needInt(String path) throws InvalidConfigurationException;

    long needLong(String path) throws InvalidConfigurationException;

    double needDouble(String path) throws InvalidConfigurationException;

    boolean needBoolean(String path) throws InvalidConfigurationException;

    String needString(String path) throws InvalidConfigurationException;

    List<?> needList(String path) throws InvalidConfigurationException;

    <T> List<T> needList(String path, Class<T> type) throws InvalidConfigurationException;
}
