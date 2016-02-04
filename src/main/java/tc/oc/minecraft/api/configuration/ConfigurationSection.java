package tc.oc.minecraft.api.configuration;

import java.util.Collection;
import java.util.List;

public interface ConfigurationSection {

    ConfigurationSection getSection(String path);

    Collection<String> getKeys();

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

    List<?> getList(String path);

    List<?> getList(String path, List<?> def);

    List<String> getStringList(String path);

    List<Byte> getByteList(String path);

    List<Short> getShortList(String path);

    List<Long> getLongList(String path);

    List<Float> getFloatList(String path);

    List<Double> getDoubleList(String path);

    List<Boolean> getBooleanList(String path);
}
