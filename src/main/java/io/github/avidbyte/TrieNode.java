package io.github.avidbyte;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Aaron
 * @since 2022-09-09 17:25
 */
public class TrieNode implements Serializable {

    int count;
    HashMap<Character, TrieNode> child;

    TrieNode() {
        this.count = 0;
        this.child = new HashMap<>();
    }

}
