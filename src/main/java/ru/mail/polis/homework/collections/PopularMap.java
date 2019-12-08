package ru.mail.polis.homework.collections;


import java.util.*;
import java.util.Map.Entry;


/**
 * Написать структуру данных, реализующую интерфейс мапы + набор дополнительных методов.
 * 2 дополнительных метода должны вовзращать самый популярный ключ и его популярность.
 * Популярность - это количество раз, который этот ключ учавствовал в других методах мапы, такие как
 * containsKey, get, put, remove.
 * Считаем, что null я вам не передю ни в качестве ключа, ни в качестве значения
 *
 * Важный момент, вам не надо реализовывать мапу, вы должны использовать композицию.
 * Вы можете использовать любые коллекции, которые есть в java.
 *
 * Помните, что по мапе тоже можно итерироваться
 *
 *         for (Map.Entry<K, V> entry : map.entrySet()) {
 *             entry.getKey();
 *             entry.getValue();
 *         }
 *
 *
 * Дополнительное задание описано будет ниже
 * @param <K> - тип ключа
 * @param <V> - тип значения
 */
public class PopularMap<K, V> implements Map<K, V> {

    private final Map<K, V> map;
    private final Map<K, Integer> keyMap = new HashMap<>();
    private final Map<V, Integer> valueMap = new HashMap<>();

    public PopularMap() {
        this.map = new HashMap<>();
    }

    public PopularMap(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        updateKeyPopularity((K) key);

        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        updateValuePopularity((V) value);

        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        V value = map.get(key);

        updateKeyPopularity((K) key);
        updateValuePopularity(value);

        return value;
    }

    @Override
    public V put(K key, V value) {
        updateKeyPopularity(key);
        updateValuePopularity(value);

        V oldValue = map.put(key, value);
        updateValuePopularity(oldValue);

        return oldValue;
    }

    @Override
    public V remove(Object key) {
        V value = map.remove(key);

        updateKeyPopularity((K) key);
        updateValuePopularity(value);

        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
        keyMap.clear();
        valueMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    /**
     * Возвращает самый популярный, на данный момент, ключ
     */
    public K getPopularKey() {
        return getPopularEntry(keyMap);
    }


    /**
     * Возвращает количество использование ключа
     */
    public int getKeyPopularity(K key) {
        return keyMap.getOrDefault(key, 0);
    }

    /**
     * Возвращает самое популярное, на данный момент, значение. Надо учесть что значени может быть более одного
     */
    public V getPopularValue() {
        return getPopularEntry(valueMap);
    }

    /**
     * Возвращает количество использований значений в методах: containsValue, get, put (учитывается 2 раза, если
     * старое значение и новое - одно и тоже), remove (считаем по старому значению).
     */
    public int getValuePopularity(V value) {
        return valueMap.getOrDefault(value, 0);
    }

    /**
     * Вернуть итератор, который итерируется по значениям (от самых НЕ популярных, к самым популярным)
     */
    public Iterator<V> popularIterator() {
        return valueMap
                .entrySet()
                .stream()
                .sorted(Entry.comparingByValue())
                .map(Entry::getKey)
                .iterator();
    }

    private void updateKeyPopularity(K key) {
        if (key == null)
            return;

        keyMap.put(key, keyMap.getOrDefault(key, 0) + 1);
    }

    private void updateValuePopularity(V value) {
        if (value == null)
            return;

        valueMap.put(value, valueMap.getOrDefault(value, 0) + 1);
    }

    private <T> T getPopularEntry(Map<T, Integer> map) {
        return map
                .entrySet()
                .stream()
                .max(Entry.comparingByValue())
                .get()
                .getKey();
    }
}
