package cn.org.alice;

import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;

public abstract class AdapterContextConsumer<G extends Annotation, K, B extends Annotation, T extends Annotation, P>
        extends AdapterContext<G, K, B, T, P> implements BiConsumer<K, P> {

    @Override
    public void accept(K key, P source) {
        super.apply(key, source);
    }
}
