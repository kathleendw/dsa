import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;
    public static Graph graph;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int pos = in.nextInt(), terowongan = in.nextInt();
        graph = new Graph();
        for (int i = 1; i <= pos; i++) {
            Vertex vertex = new Vertex(i);
            graph.adjlist.add(vertex);
        }

        for (int i = 0; i < terowongan; i++) {
            int A = in.nextInt(), B = in.nextInt(), L = in.nextInt(), S = in.nextInt();
            Edge edgeSatu = new Edge(graph.adjlist.get(A-1), graph.adjlist.get(B-1), L, S);
            Edge edgeDua = new Edge(graph.adjlist.get(B-1), graph.adjlist.get(A-1), L, S);
            (graph.adjlist.get(A-1).adj).add(edgeSatu);
            (graph.adjlist.get(B-1).adj).add(edgeDua);
        }

        int kurcaci = in.nextInt();
        for (int i = 0; i < kurcaci; i++) {
            int R = in.nextInt();
            for (int j = 0; j < graph.adjlist.size(); j++) {
                if (graph.adjlist.get(j).value == R) {
                    graph.adjlist.get(j).kurcaci = true;
                }
            }
        }

        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            if (query.equals("KABUR")) {
                int F = in.nextInt(), E = in.nextInt();
                System.out.println(graph.kabur(pos, F, E));
            } else if (query.equals("SIMULASI")) {
                int K = in.nextInt();
                int[] exit = new int[K];
                for (int j = 0; j < K; j++) {
                    int V = in.nextInt();
                    exit[j] = V;
                    graph.adjlist.get(V-1).exit = true;
                }
                System.out.println(graph.simulasi(graph, exit, pos));
                graph.resetExit();
            } else {
                int K = in.nextInt(), L = in.nextInt(), M = in.nextInt();
                System.out.println(graph.kekuatanSuper(graph, pos, K, L, M));
            }
        }
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}

class Edge {
    Vertex source, destination;
    int panjang;
    int ukuran;

    public Edge(Vertex src, Vertex dest, int L, int S) {
        this.source = src;
        this.destination = dest;
        this.panjang = L;
        this.ukuran = S;
    }

    @Override
    public String toString() {
        return this.destination.toString() + " dengan panjang " + Integer.toString(this.panjang) + " dan ukuran " + Integer.toString(this.ukuran) + "\n";
    }
}

class Vertex {
    public int value;
    public List<Edge> adj;
    public int detik;
    public int ukuran;
    public boolean kurcaci;
    public boolean exit;

    public Vertex (int val) {
        this.value = val;
        this.adj = new LinkedList<Edge>();
        this.kurcaci = false;
        this.exit = false;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
} 

class HeapNode {
    int vertex;
    int detik;
}

class MinHeap {
    int size;
    int currentSize;
    HeapNode[] heap;
    int[] indexes;

    public MinHeap(int size) {
        this.size = size;
        heap = new HeapNode[size + 1];
        indexes = new int[size];
        heap[0] = new HeapNode();
        heap[0].vertex = -1;
        heap[0].detik = Integer.MIN_VALUE;
        currentSize = 0;
    }

    public void insert(HeapNode node) {
        currentSize++;
        int index = currentSize;
        heap[index] = node;
        indexes[node.vertex-1] = index;
        bubbleUp(index);
    }

    public void bubbleUp(int pos) {
        int parent = pos/2;
        int current = pos;
        while (current > 0 && heap[parent].detik > heap[current].detik) {
            HeapNode currentNode = heap[current];
            HeapNode parentNode = heap[parent];

            indexes[currentNode.vertex-1] = parent;
            indexes[parentNode.vertex-1] = current;
            swap(current, parent);
            current = parent;
            parent = parent/2;
        }
    }

    public HeapNode extractMin() {
        HeapNode min = heap[1];
        HeapNode lastNode = heap[currentSize];
        indexes[lastNode.vertex-1] = 1;
        heap[1] = lastNode;
        heap[currentSize] = null;
        heapify(1);
        currentSize--;
        return min;
    }

    public void heapify(int i) {
        int left = 2 * i;
        int right = 2 * i + 1;
        int smallest = i;

        if (left < heapSize() && heap[smallest].detik > heap[left].detik) {
            smallest = left;
        }
        if (right < heapSize() && heap[smallest].detik > heap[right].detik) {
            smallest = right;
        }
        if (smallest != i) {
            HeapNode smallestNode = heap[smallest];
            HeapNode node = heap[i];

            indexes[smallestNode.vertex-1] = i;
            indexes[node.vertex-1] = smallest;
            swap(i, smallest);
            heapify(smallest);
        }
    }

    public void swap(int x, int y) {
        HeapNode temp = heap[x];
        heap[x] = heap[y];
        heap[y] = temp;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public int heapSize() {
        return currentSize;
    }
}

class Graph {  
    List<Vertex> adjlist = new ArrayList<Vertex>();

    public void Graph() {
        return;
    }
    
    // inspirasi: https://www.geeksforgeeks.org/widest-path-problem-practical-application-of-dijkstras-algorithm/
    public int kabur(int pos, int src, int dst) {
        Queue<Vertex> queue = new LinkedList<>();
        Vertex start = adjlist.get(src-1);
        start.ukuran = 0;
        queue.add(start);

        int[] arr = new int[pos];
        for (int i = 0; i < pos; i++) {
            arr[i] = Integer.MIN_VALUE;
        }
        arr[src-1] = Integer.MAX_VALUE;
        
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
    
            for (Edge e: v.adj) {
                int dist = Math.max(arr[e.destination.value-1], Math.min(arr[v.value-1], e.ukuran));
                
                if (dist > arr[e.destination.value-1]) {
                    arr[e.destination.value-1] = dist;
                    Vertex newVertex = e.destination;
                    newVertex.ukuran = dist;
                    queue.add(newVertex);
                }
            }
        }
        return arr[dst-1];
    } 

    // inspirasi: Lab 6 dan Lab 7
    public int simulasi(Graph graph, int[] exit, int pos) {
        int[] jarakTerkecil = new int[pos + 1];
        for (int j = 0; j < pos; j++) {
            jarakTerkecil[j+1] = Integer.MAX_VALUE;
        }

        HeapNode[] heapNodes = new HeapNode[pos];
        for (int i: exit) {
            graph.dijkstra(heapNodes, graph, i, pos);
            for (int j = 1; j < jarakTerkecil.length; j++) {
                if ((graph.adjlist.get(j-1).exit == true) && (graph.adjlist.get(j-1).kurcaci == true)) {
                    jarakTerkecil[j] = 0;
                } else if ((jarakTerkecil[j] == 0) || (jarakTerkecil[j] > heapNodes[j-1].detik)) {
                    jarakTerkecil[j] = heapNodes[j-1].detik;
                }
            }
        }

        int res = 0;
        for (int i = 0; i < graph.adjlist.size(); i++) {
            if ((graph.adjlist.get(i).kurcaci == true) && (jarakTerkecil[i+1] > res)) {
                res = jarakTerkecil[i+1];
            }
        } 

        return res;
    }

    // inspirasi: https://algorithms.tutorialhorizon.com/dijkstras-shortest-path-algorithm-spt-adjacency-list-and-min-heap-java-implementation/
    public void dijkstra(HeapNode[] heapNodes, Graph graph, int V, int pos) {
        boolean[] isVisited = new boolean[pos + 1];

        for (int i = 0; i < pos; i++) {
            heapNodes[i] = new HeapNode();
            heapNodes[i].vertex = i+1;
            heapNodes[i].detik = Integer.MAX_VALUE;
        }
        heapNodes[V-1].detik = 0;

        MinHeap minHeap = new MinHeap(pos);
        for (int i = 0; i < pos ; i++) {
            minHeap.insert(heapNodes[i]);
        }

        while(!minHeap.isEmpty()){
            int v = minHeap.extractMin().vertex;
            isVisited[v] = true;

            for (Edge e: graph.adjlist.get(v-1).adj) {
                int dest = e.destination.value;
                if (isVisited[dest] == false) {
                    int x =  heapNodes[v-1].detik + e.panjang;
                    int y = heapNodes[dest-1].detik;
                    if (y > x) {
                        decrease(minHeap, x, dest);
                        heapNodes[dest-1].detik = x;
                    }
                }
            }
        }
    }

    public void decrease(MinHeap minHeap, int x, int v){
        int index = minHeap.indexes[v-1];
        HeapNode heapNode = minHeap.heap[index];
        heapNode.detik = x;
        minHeap.bubbleUp(index);
    }

    public int kekuatanSuper(Graph graph, int pos, int K, int L, int M) {
        Vertex start = adjlist.get(K-1);
        Vertex jump = adjlist.get(L-1);
        Vertex end = adjlist.get(M-1);
        boolean[] isVisitedFirst = new boolean[pos];
        List<Integer> temp = new ArrayList<Integer>();
        List<List<Integer>> resultFirst = new ArrayList<List<Integer>>();
        boolean[] isVisitedSecond = new boolean[pos];
        List<List<Integer>> resultSecond = new ArrayList<List<Integer>>();
        List<Integer> hasil = new ArrayList<Integer>();

        pathlistsuper(start, jump, isVisitedFirst, temp, resultFirst);
        for (List<Integer> i: resultFirst) {
            pathlistsuper(jump, end, isVisitedSecond, i, resultSecond);
        }

        for (int i = 0; i < resultSecond.size(); i++) {
            int max = 0;
            int sum = 0;
            for (int j = 0; j < resultSecond.get(i).size(); j++) {
                if (resultSecond.get(i).get(j) > max) {
                    max = resultSecond.get(i).get(j);
                }
            }
            for (int j = 0; j < resultSecond.get(i).size(); j++) {
                if (resultSecond.get(i).get(j) == max) {
                    resultSecond.get(i).set(j, 0);
                    break;
                }
            }
            for (int j = 0; j < resultSecond.get(i).size(); j++) {
                sum += resultSecond.get(i).get(j);
            }
            hasil.add(sum);
        }

        int res = Integer.MAX_VALUE;
        for (int i = 0; i < hasil.size(); i++) {
            if (res > hasil.get(i)) {
                res = hasil.get(i);
            }
        } 
        
        return res;
    }

    // inspirasi: https://www.geeksforgeeks.org/find-paths-given-source-destination/
    public void pathlistsuper(Vertex start, Vertex end, boolean[] isVisited, List<Integer> temp, List<List<Integer>> result) {
        if (start.value == end.value) {
            List<Integer> res = new ArrayList<Integer>();
            for(int i: temp)
                res.add(i);
            result.add(res);
            return;
		}

		isVisited[start.value-1] = true;
		for (Edge e: start.adj) {
			if (!isVisited[e.destination.value-1]) {
                Vertex v = e.destination;
                int u = e.panjang;
                temp.add(u);
				pathlistsuper(v, end, isVisited, temp, result);
                temp.remove(Integer.valueOf(u));
			}
		}
		isVisited[start.value-1] = false;
    } 

    public void resetExit() {
        for (Vertex v: adjlist) {
            v.exit = false;
        }
    }
}