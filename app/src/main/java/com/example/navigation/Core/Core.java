package com.example.navigation.Core;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.util.*;

public class Core {

    public static AssetManager assetManager;
    /**
     * Количество рёбер графа * 2
     */
    private static final int SIZE = 71 * 2;
    /**
     * Номер стартового узла
     */
    private static int startI = 1;

    private static int startMax = 0;
    private static int endMax = 0;
    /**
     * Номер конечного узла
     */
    private static final int endI = 5;

    public static String main(int startNew, int endNew) {

        int[][] array = new int[SIZE][3];

        HashMap<Integer, Node> graph = createGraph(Objects.requireNonNull(readFile(array)));

        Node start = graph.get(setBuilding(startNew));
        Node end = graph.get(setBuilding(endNew));
        LinkedList<Node> linkedList = getShortestPath(graph,
                start,
                end);

        if (linkedList != null) {
            return getNode(linkedList);
        } else {
            return "-1";
        }

    }

    /**
     * Вывод пути
     */
    public static String getNode(LinkedList<Node> path) {
        int sum = 0;
        StringBuilder strPath = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            strPath.append(path.get(i).value).append(" ");
            if (i != path.size() - 1)
                sum += path.get(i).parents.get(path.get(i + 1)).weight;
        }
        return strPath.toString();
    }

    /**
     * Дейкстра
     */
    public static LinkedList<Node> getShortestPath(HashMap<Integer, Node> graph, Node start, Node end) {
        HashSet<Node> unprocessedNodes = new HashSet<>();
        HashMap<Node, Integer> timeToNodes = new HashMap<>();
        initHashTables(start, graph, unprocessedNodes, timeToNodes);
        calculateTimeToEachNode(unprocessedNodes, timeToNodes);
        if (timeToNodes.get(end) == null) return null;
        return getShortestPath(start, end, timeToNodes);
    }

    public static LinkedList<Node> getShortestPath(Node start, Node end, HashMap<Node, Integer> timeToNodes) {
        LinkedList<Node> path = new LinkedList<>();
        Node node = end;
        while (node != start) {
            int minTimeToNode = timeToNodes.get(node);
            path.addFirst(node);
            for (Map.Entry<Node, Edge> parentAndEdge : node.parents.entrySet()) {
                Node parent = parentAndEdge.getKey();
                Edge parentEdge = parentAndEdge.getValue();
                if (!timeToNodes.containsKey(parent)) continue;
                boolean prevNodeFound = (parentEdge.weight + timeToNodes.get(parent)) == minTimeToNode;
                if (prevNodeFound) {
                    timeToNodes.remove(node);
                    node = parent;
                    break;
                }
            }
        }
        path.addFirst(node);
        return path;
    }

    public static void calculateTimeToEachNode(HashSet<Node> unprocessedNodes,
                                               HashMap<Node, Integer> timeToNodes) {
        while (!unprocessedNodes.isEmpty()) {
            Node node = getNodeWithMinTimeTolt(unprocessedNodes, timeToNodes);
            if (node == null) return;
            if (timeToNodes.get(node) == Integer.MAX_VALUE) return;
            for (Edge edge : node.edges) {
                Node adjacentNode = edge.adjacentNode;
                if (unprocessedNodes.contains(adjacentNode)) {
                    int timeToCheck = timeToNodes.get(node) + edge.weight;
                    if (timeToCheck < timeToNodes.get(adjacentNode))
                        timeToNodes.put(adjacentNode, timeToCheck);
                }
            }
            unprocessedNodes.remove(node);
        }
    }

    public static Node getNodeWithMinTimeTolt(HashSet<Node> unprocessedNodes,
                                              HashMap<Node, Integer> timeToNodes) {
        Node nodeWithMinTimeTolt = null;

        int minTime = Integer.MAX_VALUE;
        for (Node node : unprocessedNodes) {
            int time = timeToNodes.get(node);
            if (time < minTime) {
                minTime = time;
                nodeWithMinTimeTolt = node;
            }
        }
        return nodeWithMinTimeTolt;
    }

    /**
     * Стартовые данные
     */
    public static void initHashTables(Node start, HashMap<Integer, Node> graph, HashSet<Node> unprocessedNode,
                                      HashMap<Node, Integer> timeToNodes) {
        for (Map.Entry<Integer, Node> mapEntry : graph.entrySet()) {
            Node node = mapEntry.getValue();
            unprocessedNode.add(node);
            timeToNodes.put(node, Integer.MAX_VALUE);
        }
        timeToNodes.put(start, 0);
    }

    /**
     * Получение или добавление
     */
    public static Node addOrGetNode(HashMap<Integer, Node> graph, int value) {
        if (value == -1) return null;
        if (graph.containsKey(value)) return graph.get(value);

        Node node = new Node(value);
        graph.put(value, node);
        return node;
    }

    /**
     * Генерация графа
     */
    public static HashMap<Integer, Node> createGraph(int[][] graphData) {
        HashMap<Integer, Node> graph = new HashMap<>();
        for (int[] row : graphData) {
            Node node = addOrGetNode(graph, row[0]);
            Node adjacentNode = addOrGetNode(graph, row[1]);
            if (adjacentNode == null) continue;

            Edge edge = new Edge(adjacentNode, row[2]);
            assert node != null;
            node.edges.add(edge);
            adjacentNode.parents.put(node, edge);

        }

        return graph;
    }

    public static int[][] readFile(int[][] array) {
        try {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(assetManager.open("GraphList")));
            String line = reader.readLine();


            int i = 0;
            while (line != null) {
                String[] str = line.replace("--", "")
                        .replace("  ", " ")
                        .split(" ");

                if (Objects.equals(str[0], str[1])) {
                    System.out.println("Неправильные входные данные");
                    break;
                }

                int node1 = Integer.parseInt(str[0]);
                int node2 = Integer.parseInt(str[1]);
                int weight = getWeight(str[2]);

                array[i][0] = node1;
                array[i + 1][0] = node2;

                array[i][1] = node2;
                array[i + 1][1] = node1;

                array[i][2] = weight;
                array[i + 1][2] = weight;

                if (endMax < node1) endMax = node1;
                if (startMax < node2) startMax = node1;

                i = i + 2;
                line = reader.readLine();
            }
            chekEndStart();
            return array;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static int getWeight(String s) {
        if (s.contains(".")) return Integer.parseInt(s.replace(".", ""));
        return Integer.parseInt(s) * 10;
    }

    /**
     * Сопоставление корпусов(на вход номер корпуса)
     */
    public static int setBuilding(int number) {
        int result;
        switch (number) {
            case 2:
                result = 54;
                break;
            case 3:
                result = 36;
                break;
            case 4:
                result = 45;
                break;
            case 5:
                result = 4;
                break;
            case 6:
                result = 7;
                break;
            case 7:
                result = 49;
                break;
            case 8:
                result = 33;
                break;
            case 9:
                result = 60;
                break;
            case 10:
                result = 23;
                break;
            case 11:
                result = 41;
                break;
            case 12:
                result = 53;
                break;
            case 13:
                result = 38;
                break;
            case 14:
                result = 20;
                break;
            default:
                result = 1;
                break;
        }
        return result;
    }

    public static void chekEndStart() {
        try {
            if (endMax < endI || startMax < startI) {
                startI = 1;
                throw new Exception("No date");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String getPathRoute(AssetManager assetManager1, int startNew, int endNew) {

        assetManager = assetManager1;
        String strPathRoute = "";
        String[] strPathNode = (main(startNew, endNew)).split(" ");
        Map<String, String> strPathPath = new HashMap<>();

        try {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(assetManager.open("Path")));
            String line = reader.readLine();
            while (line != null) {
                String[] str = line.split(" ");
                strPathPath.put(str[0], str[1]);

                line = reader.readLine();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


        for (int i = 0; i < strPathNode.length; i++) {
            strPathRoute += strPathPath.get(strPathNode[i]) + " ";
        }


        return strPathRoute;
    }

}
