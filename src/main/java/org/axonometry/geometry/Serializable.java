package org.axonometry.geometry;

public interface Serializable<T> {
    String toString();
    T fromString(String str);
}
