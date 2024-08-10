
import java.io.*;
import java.util.*;

class Node implements Comparable<Node> {
    int x, y;
    int distance;

    Node(int x, int y, int distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }
}

public class Solution {
    static char[][] grid;  // 2D array to represent the grid
    static int[] dx = {0, 0, 1, -1};  // Changes in x-coordinate for four directions
    static int[] dy = {1, -1, 0, 0};  // Changes in y-coordinate for four directions
    static int startX, startY, endX, endY;  // Start and end coordinates

    public static void main(String[] args) throws IOException {
        // Path to the directory containing input files
        String directoryPath = "C:\\Users\\thama\\Desktop\\20220476_Thanuri\\benchmark_series (1)\\benchmark_series";
        // Create a File object representing the directory
        File directory = new File(directoryPath);
        // Get a list of files in the directory
        File[] files = directory.listFiles();

        // Check if the directory exists
        if (files != null) {
            // Iterate through each file in the directory
            for (File file : files) {
                // Process only regular files
                if (file.isFile()) {
                    // Print the name of the file being processed
                    System.out.println("Processing file: " + file.getName());
                    // Read the lines from the file and store them in a list
                    List<String> lines = readFileLines(file);
                    // Process the lines from the file
                    processFile(lines);
                }
            }
        } else {
            // Print a message if the directory does not exist
            System.out.println("Directory not found: " + directoryPath);
        }
    }

    // Read lines from a file and return them as a list of strings
    private static List<String> readFileLines(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        // Read each line from the file
        while ((line = br.readLine()) != null) {
            // Add the line to the list of lines
            lines.add(line);
        }
        // Close the BufferedReader
        br.close();
        // Return the list of lines read from the file
        return lines;
    }

    // Process the lines read from a file
    private static void processFile(List<String> lines) {
        // Initialize the grid with dimensions based on the number of lines and length of each line
        grid = new char[lines.size()][lines.get(0).length()];
        // Iterate through each line
        for (int i = 0; i < lines.size(); i++) {
            // Convert the line to a character array and store it in the grid
            grid[i] = lines.get(i).toCharArray();
            // Iterate through each character in the line
            for (int j = 0; j < grid[i].length; j++) {
                // If the character is 'S', set the start coordinates
                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                }
                // If the character is 'F', set the end coordinates
                else if (grid[i][j] == 'F') {
                    endX = i;
                    endY = j;
                }
            }
        }

        // Print the start and end coordinates
        System.out.println("Start: (" + startX + ", " + startY + ")");
        System.out.println("End: (" + endX + ", " + endY + ")");

        // Run Dijkstra's algorithm to find the shortest path
        dijkstra();
    }

    // Implementation of Dijkstra's algorithm
    static void dijkstra() {
        // Print a message indicating that Dijkstra's algorithm is being executed
        System.out.println("Running Dijkstra algorithm...");
        // Initialize 2D arrays to store distances and previous nodes
        int[][] distance = new int[grid.length][grid[0].length];
        Node[][] prev = new Node[grid.length][grid[0].length];
        // Create a priority queue to store nodes
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Initialize distance array with maximum values
        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }

        // Set distance to start node as 0 and add it to the priority queue
        distance[startX][startY] = 0;
        pq.offer(new Node(startX, startY, 0));

        // Dijkstra's algorithm loop
        while (!pq.isEmpty()) {
            // Extract the node with the minimum distance from the priority queue
            Node curr = pq.poll();

            // If the current node is the destination, print the shortest path and exit
            if (curr.x == endX && curr.y == endY) {
                System.out.println("Shortest path length: " + curr.distance);
                printPath(prev, curr);
                return;
            }

            // Explore neighbors of the current node
            for (int i = 0; i < 4; i++) {
                int nx = curr.x + dx[i];
                int ny = curr.y + dy[i];
                // Check if the neighbor is within the grid boundaries and is not an obstacle
                if (nx < 0 || nx >= grid.length || ny < 0 || ny >= grid[0].length || grid[nx][ny] == '0') continue;
                // Calculate the new distance to the neighbor
                int newDistance = curr.distance + 1;
                // Update the distance and previous node if the new distance is shorter
                if (newDistance < distance[nx][ny]) {
                    distance[nx][ny] = newDistance;
                    prev[nx][ny] = curr;
                    // Add the neighbor to the priority queue
                    pq.offer(new Node(nx, ny, newDistance));
                }
            }
        }
    }

    // Print the shortest path
    static void printPath(Node[][] prev, Node end) {
        // Print a message indicating the start of the path
        System.out.println("Path:");
        // Initialize step counter
        int step = 1;
        Node curr = end;
        // Print the starting point
        System.out.println(step + ". Start at (" + (curr.y + 1) + "," + (curr.x + 1) + ")");

        // Traverse the path backwards from the end to the start
        while (curr != null) {
            // If the previous node exists, calculate the direction and print the movement
            if (prev[curr.x][curr.y] != null) {
                int dx = curr.x - prev[curr.x][curr.y].x;
                int dy = curr.y - prev[curr.x][curr.y].y;
                String direction;
                if (dx == 0 && dy == 1) {
                    direction = "right";
                } else if (dx == 0 && dy == -

                        1) {
                    direction = "left";
                } else if (dx == 1 && dy == 0) {
                    direction = "down";
                } else {
                    direction = "up";
                }
                // Print the movement direction
                System.out.println(step + ". Move " + direction + " to (" + (prev[curr.x][curr.y].y + 1) + "," + (prev[curr.x][curr.y].x + 1) + ")");
            } else {
                // If the previous node does not exist, print that the path is complete
                System.out.println(step + ". Done!");
            }
            // Move to the previous node
            step++;
            curr = prev[curr.x][curr.y];
        }
    }
}
