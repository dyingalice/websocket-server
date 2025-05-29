package cn.org.alice;

import java.lang.annotation.Annotation;
import java.util.function.BiFunction;

public abstract class AdapterContextFunction<G extends Annotation, K, B extends Annotation, T extends Annotation, P, R>
        extends AdapterContext<G, K, B, T, P>
        implements BiFunction<K, P, R> {

    @Override
    public R apply(K key, P source) {
        return (R) super.apply(key, source);
    }

}
