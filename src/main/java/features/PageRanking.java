package features;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class AVLnodee {
    String key;
    int count;
    int heightss;
    AVLnodee leftside, rightside;

    public AVLnodee(String key) {
        this.key = key;
        this.count = 1;
        this.heightss = 1;
    }
}

class AVLTree {
    AVLnodee roots;

    // Function to get heightss of a nodee
    int heightss(AVLnodee nodee) {
        return (nodee == null) ? 0 : nodee.heightss;
    }

    // Function to get the Balancees factor of a nodee
    int getBalancees(AVLnodee nodee) {
        return (nodee == null) ? 0 : heightss(nodee.leftside) - heightss(nodee.rightside);
    }

    // Function to rightside rotate a subtree rootsed with y
    AVLnodee rightsideRotate(AVLnodee yy) {
        AVLnodee xx = yy.leftside;
        AVLnodee T2 = xx.rightside;

        xx.rightside = yy;
        yy.leftside = T2;

        yy.heightss = Math.max(heightss(yy.leftside), heightss(yy.rightside)) + 1;
        xx.heightss = Math.max(heightss(xx.leftside), heightss(xx.rightside)) + 1;

        return xx;
    }

    // Function to leftside rotate a subtree rootsed with x
    AVLnodee leftsideRotate(AVLnodee xx) {
        AVLnodee yy = xx.rightside;
        AVLnodee T2 = yy.leftside;

        yy.leftside = xx;
        xx.rightside = T2;

        xx.heightss = Math.max(heightss(xx.leftside), heightss(xx.rightside)) + 1;
        yy.heightss = Math.max(heightss(yy.leftside), heightss(yy.rightside)) + 1;

        return yy;
    }

    // Function to insert a key into the AVL tree
    AVLnodee insert(AVLnodee nodee, String key) {
        // Perform the normal BST insertion
        if (nodee == null) {
            return new AVLnodee(key);
        }

        if (key.equals(nodee.key)) {
            nodee.count++;
            return nodee;
        }

        if (key.compareTo(nodee.key) < 0) {
            nodee.leftside = insert(nodee.leftside, key);
        } else {
            nodee.rightside = insert(nodee.rightside, key);
        }

        // Update heightss of the current nodee
        nodee.heightss = 1 + Math.max(heightss(nodee.leftside), heightss(nodee.rightside));

        // Perform rotation to maintain Balancees
        int Balancees = getBalancees(nodee);

        // leftside leftside Case
        if (Balancees > 1 && key.compareTo(nodee.leftside.key) < 0) {
            return rightsideRotate(nodee);
        }

        // rightside rightside Case
        if (Balancees < -1 && key.compareTo(nodee.rightside.key) > 0) {
            return leftsideRotate(nodee);
        }

        // leftside rightside Case
        if (Balancees > 1 && key.compareTo(nodee.leftside.key) > 0) {
            nodee.leftside = leftsideRotate(nodee.leftside);
            return rightsideRotate(nodee);
        }

        // rightside leftside Case
        if (Balancees < -1 && key.compareTo(nodee.rightside.key) < 0) {
            nodee.rightside = rightsideRotate(nodee.rightside);
            return leftsideRotate(nodee);
        }

        return nodee;
    }

    // Traverse the AVL tree in-order and print results
    void inOrderTraversal(AVLnodee nodee, List<Map.Entry<String, Integer>> resultList) {
        if (nodee != null) {
            inOrderTraversal(nodee.leftside, resultList);
            resultList.add(new AbstractMap.SimpleEntry<>(nodee.key, nodee.count));
            inOrderTraversal(nodee.rightside, resultList);
        }
    }
}

public class PageRanking {
    // Function to perform Bubble Sort on the result list
    public static void bubbleSort(List<Map.Entry<String, Integer>> resultList) {
        int n = resultList.size();
        for (int a = 0; a < n - 1; a++) {
            for (int b = 0; b < n - a - 1; b++) {
                if (resultList.get(b).getValue() < resultList.get(b + 1).getValue()) {
                    // swap temp and arr[i]
                    Map.Entry<String, Integer> temp = resultList.get(b);
                    resultList.set(b, resultList.get(b + 1));
                    resultList.set(b + 1, temp);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Path file1Path = Paths.get("C:\\Users\\Bivek\\Documents\\ACC\\ACC_Project\\src\\main\\resources\\cargurus.ca.csv");
            Path file2Path = Paths.get("C:\\Users\\Bivek\\Documents\\ACC\\ACC_Project\\src\\main\\resources\\driveaxis.ca.csv");
            Path file3Path = Paths.get("C:\\Users\\Bivek\\Documents\\ACC\\ACC_Project\\src\\main\\resources\\goauto.ca.csv");

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Enter a word for Frequency and page ranking calculation: ");
                String userInputWord = scanner.next().toLowerCase();

                AVLTree avlTree = new AVLTree();

                // Process each file
                for (Path filePath : Arrays.asList(file1Path, file2Path, file3Path)) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] words = line.toLowerCase().split("\\s+");
                            for (String word : words) {
                                if (word.contains(userInputWord)) {
                                    avlTree.roots = avlTree.insert(avlTree.roots, filePath.getFileName().toString());
                                }
                            }
                        }
                    }
                }

                // Calculate total frequency
                List<Map.Entry<String, Integer>> resultList = new ArrayList<>();
                avlTree.inOrderTraversal(avlTree.roots, resultList);
                int totalFrequency = resultList.stream().mapToInt(Map.Entry::getValue).sum();
                System.out.println("Total frequency of '" + userInputWord + "' across all files: " + totalFrequency);

                // Print page ranking after Bubble Sort
                bubbleSort(resultList);
                System.out.println("\nPage ranking of '" + userInputWord + "' :");
                for (int i = 0; i < resultList.size(); i++) {
                    System.out.println((i + 1) + ". " + resultList.get(i).getKey() + ": " + resultList.get(i).getValue() + " occurrences");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
