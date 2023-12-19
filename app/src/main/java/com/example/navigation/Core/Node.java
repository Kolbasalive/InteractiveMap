package com.example.navigation.Core;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/** Узел */
class Node{
    /** Значение узла (номер узла)*/
    int value;
    /** Список рёбер */
    LinkedHashSet<Edge> edges = new LinkedHashSet<>();
    /** Список родителей */
    LinkedHashMap<Node,Edge> parents = new LinkedHashMap<>();

    public Node(int value){
        this.value = value;
    }

}