package io.github.dulidanci.whereroadsmeet.util;

import java.util.Objects;

public record Pair<A, B>(A first, B second) {

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return "Pair[" + first + ", " + second + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?, ?>(Object first1, Object second1))) return false;
        return Objects.equals(first, first1)
            && Objects.equals(second, second1);
    }

}
