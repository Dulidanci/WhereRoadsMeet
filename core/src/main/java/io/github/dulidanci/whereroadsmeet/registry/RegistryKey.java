package io.github.dulidanci.whereroadsmeet.registry;

import java.util.Objects;

public record RegistryKey(String id, int number) {
    public static RegistryKey of(String id, int number) {
        if (id == null || Objects.equals(id, "") || number < 0)
            throw new IllegalArgumentException("Invalid RegistryKey parameters: " + id + "; " + number);
        return new RegistryKey(id, number);
    }

    public static RegistryKey halfKey(int number) {
        return new RegistryKey("", number);
    }

    public static RegistryKey halfKey(String id) {
        return new RegistryKey(id, -1);
    }

    @Override
    public String toString() {
        return "RegistryKey[" + id + ", " + number + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RegistryKey(String objId, int objNumber))) return false;
        return Objects.equals(id, objId)
            || Objects.equals(number, objNumber);
    }
}
