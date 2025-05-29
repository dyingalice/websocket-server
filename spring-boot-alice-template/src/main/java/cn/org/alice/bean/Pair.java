package cn.org.alice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 键值对
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<K, V> {

    private K key;

    private V value;
}
