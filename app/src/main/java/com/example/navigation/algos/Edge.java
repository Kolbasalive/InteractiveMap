package com.example.navigation.algos;

/** Рёбра */
class Edge{
    /** Ссылка на узел, на которое это ребро ведёт */
    Node adjacentNode;
    /** Вес ребра */
    int weight;

    public Edge(Node adjacentNode, int weight){
        this.adjacentNode = adjacentNode;
        this.weight = weight;
    }

}