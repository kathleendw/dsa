import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi Class Lantai
class Mesin {
    public int tingkatPopularitas;
    public int nomorIdentitas;
    public int[] skorMesin;

    public Mesin(int nomorIdentitas, int[] skorMesin) {
        this.nomorIdentitas = nomorIdentitas;
        this.skorMesin = skorMesin;
    }

    @Override
    public String toString() {
        return Integer.toString(nomorIdentitas) + " " + Arrays.toString(skorMesin);
    }

    public int getTingkatPopularitas(){
        return skorMesin.length;
    }

    public int getNomorIdentitas(){
        return nomorIdentitas;
    }

    public int[] getSkorMesin(){
        return skorMesin;
    }
}

class Permainan {
    Node head, tail;
    int size;

    class Node {
        Mesin mesin;
        Node prev, next;
        boolean budi = false;

        Node(Mesin x) { 
            mesin = x; 
        }
    }

    public Permainan() {
        head = new Node(null);
        tail = new Node(null);
        head.budi = true;
        head.next = tail;
        tail.prev = head;
    }

    public void insertNode(Mesin mesin) {
        Node newNode = new Node(mesin);
 
        if (size == 0) {
            newNode.prev = head;
            head.next = newNode;
            newNode.next = tail;
            tail.prev = newNode;
            head.budi = false;
            head.next.budi = true;
            size++;
            return;
        } else {
            Node current = tail.prev;
            current.next = newNode;
            newNode.prev = current;
            tail.prev = newNode;
            newNode.next = tail;
            head.budi = false;
            head.next.budi = true;
            size++;
        }
    }

    public void printlist() {
        Node current = head.next;
        while (current.mesin != null) {
            System.out.print(current.mesin.getTingkatPopularitas() + " " + current.mesin + " " + current.budi + "\n");
            current = current.next;
        }
        System.out.println();
    }

    public void sortList() {
        Node current = head.next;
        while (current.mesin != null) {
            insertionSortArr(current.mesin.skorMesin);
            current = current.next;
        }
    }

    public void insertionSortArr(int[] arr) {
		int N = arr.length;
		for (int i = 1; i < N; i++) {
			int temp = arr[i];
			int j = i;

			while ((j > 0) && (arr[j-1] < temp)) {
				arr[j] = arr[j-1];
				j--;
			}
			arr[j] = temp;
		}
	}

    public long main(int skorBudi) {
        Node current = head.next;
        for (int i = 0; i < size; i++) {
            if (current.budi == true) {
                break;
            }
            current = current.next;
        }
        
        int N = current.mesin.skorMesin.length;
        int arr[] = new int[N + 1];
        for (int i = 0; i < N; i++) {
            arr[i] = (current.mesin.skorMesin)[i];
        }
        arr[N] = skorBudi;
        current.mesin.skorMesin = arr;

        insertionSortArr(current.mesin.skorMesin);

        for (int i = 0; i < current.mesin.skorMesin.length; i++) {
            if ((current.mesin.skorMesin)[i] == skorBudi) {
                return i+1;
            }
        }
        return 0;
    } 

    public long gerak(String arah) {
        Node current = head.next;
        for (int i = 0; i < size; i++) {
            if (current.budi == true) {
                break;
            }
            current = current.next;
        }

        current.budi = false;
        if (arah.equals("KIRI")) {
            if (current.prev.mesin != null) {
                current = current.prev;
                current.budi = true;
            } else {
                current = tail.prev;
                tail.prev.budi = true;
            } 
        } else {
            if (current.next.mesin != null) {
                current = current.next;
                current.budi = true;
            } else {
                current = head.next;
                head.next.budi = true;
            }
        }
        return current.mesin.nomorIdentitas;
    }

    public long hapus(int skorCurang) {
        Node current = head.next;
        for (int i = 0; i < size; i++) {
            if (current.budi == true) {
                break;
            }
            current = current.next;
        }

        int length = current.mesin.skorMesin.length;
        int res[] = new int[skorCurang];
        int sum = 0;

        if (length <= skorCurang) {
            int arr[] = new int[0];
            res = current.mesin.skorMesin;
            current.mesin.skorMesin = arr;
            if (current.next.mesin == null) {
                current.budi = false;
                head.next.budi = true; 
            } else if ((current.prev.mesin == null) && (current.next.mesin == null)) {
                current.budi = true;
            } else {
                current.budi = false;
                current.next.budi = true;
                Node newNode = new Node(current.mesin);
                Node last = tail.prev;
                last.next = newNode;
                newNode.prev = last;
                tail.prev = newNode;
                newNode.next = tail;
                current.next.prev = current.prev;
                current.prev.next = current.next;
                
            }
        } else {
            int arr[] = new int[length - skorCurang];
            for (int i = 0, j = 0; i < length; i++) {
                if ((i >= 0) && (i < skorCurang)) {
                    res[i] = (current.mesin.skorMesin)[i];
                    continue;
                }
                arr[j++] = (current.mesin.skorMesin)[i];
            }
            current.mesin.skorMesin = arr;
        }
        for (int i = 0; i < res.length; i++) {
            sum += res[i];
        }

        return sum;
    }

    public long lihat(int lowerBound, int upperBound) {
        Node current = head.next;
        for (int i = 0; i < size; i++) {
            if (current.budi == true) {
                break;
            }
            current = current.next;
        }

        int length = current.mesin.skorMesin.length;
        int count = 0;
        for (int i = 0, j = 0; i < length; i++) {
            if (((current.mesin.skorMesin)[i] >= lowerBound) && ((current.mesin.skorMesin)[i] <= upperBound)) {
                count++;
            }
        }
        return count;
    }

    public int evaluasi(Node head) {
        return 0;
    }

    /* public Node insertionSortList(Node sortedHead) {
        Node sorted = null;
        Node current = sortedHead;

        while (current != null) {
            current.prev = current.next = null;
            sorted = sortedInsert(sorted, current);
            current = current.next;
        }
        sortedHead = sorted;
        return sortedHead;
    }

    public Node sortedInsert(Node sortedHead, Node node) {
        // Special case for the head end 
        if (sortedHead == null) {
            sortedHead = node;
        } else if (sortedHead.mesin.getTingkatPopularitas() >= node.mesin.getTingkatPopularitas()) {
            node.next = sortedHead;
            node.next.prev = node;
            sortedHead = node;
        } else {
            Node current = sortedHead;
            while (current.next != null && current.next.mesin.getTingkatPopularitas() < node.mesin.getTingkatPopularitas()) {
                current = current.next;
            }
            node.next = current.next;
            if (current.next != null) {
                node.next.prev = node;
            }
            current.next = node;
            node.prev = current;
        }
        return sortedHead;
    } */

    public Node sortedInsert(Node head_ref, Node newNode)
    {

        // if list is empty
        if (head_ref == null)
            head_ref = newNode;

        // if the node is to be inserted at the beginning
        // of the doubly linked list
        else if ((newNode.mesin != null) && (head_ref).mesin.getTingkatPopularitas() <= newNode.mesin.getTingkatPopularitas())
        {
            newNode.next = head_ref;
            newNode.next.prev = newNode;
            head_ref = newNode;
        }

        else
        {
            Node current = head_ref;

            // locate the node after which the new node
            // is to be inserted
            while (current.next != null && (newNode.mesin != null) &&
                current.next.mesin.getTingkatPopularitas() > newNode.mesin.getTingkatPopularitas())
                current = current.next;

            //Make the appropriate links /

            newNode.next = current.next;

            // if the new node is not inserted
            // at the end of the list
            if (current.next != null)
                newNode.next.prev = newNode;

            current.next = newNode;
            newNode.prev = current;
        }
        return head_ref;
    }

    // function to sort a doubly linked list using insertion sort
    public Node insertionSort(Node head_ref)
    {
        // Initialize 'sorted' - a sorted doubly linked list
        Node sorted = null;

        // Traverse the given doubly linked list and
        // insert every node to 'sorted'
        Node current = head_ref;
        while (current != null)
        {

            // Store next for next iteration
            Node next = current.next;

            // removing all the links so as to create 'current'
            // as a new node for insertion
            current.prev = current.next = null;

            //System.out.println(current.mesin);
            // insert current in 'sorted' doubly linked list
            sorted=sortedInsert(sorted, current);

            // Update current
            current = next;
        }

        // Update head_ref to point to sorted doubly linked list
        head_ref = sorted;
        
        return head_ref;
    } 
}

public class TP2 {

    private static InputReader in;
    static PrintWriter out;
    static Mesin mesin;
    static int[] skorMesin;
    static Permainan permainan;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahMesin = in.nextInt();
        permainan = new Permainan();
        for (int i = 1; i < jumlahMesin+1; i++) {
            int tingkatPopularitas = in.nextInt();
            int nomorIdentitas = i;
            skorMesin = new int[tingkatPopularitas];
            for (int j = 0; j < tingkatPopularitas; j++) {
                int skor = in.nextInt(); 
                skorMesin[j] = skor;
                mesin = new Mesin(nomorIdentitas, skorMesin);
            }
            permainan.insertNode(mesin);
        }
        permainan.sortList();
        // permainan.printlist();

        int jumlahQuery = in.nextInt();
        for (int i = 0; i < jumlahQuery; i++) {
            String query = in.next();
            if (query.equals("MAIN")) {
                int skorBudi = in.nextInt();
                System.out.println(permainan.main(skorBudi));
                // permainan.printlist();
            } else if (query.equals("GERAK")) {
                String arah = in.next();
                System.out.println(permainan.gerak(arah));
                // permainan.printlist();
            } else if (query.equals("HAPUS")) {
                int skorCurang = in.nextInt();
                System.out.println(permainan.hapus(skorCurang));
                // permainan.printlist();
            } else if (query.equals("LIHAT")) {
                int lowerBound = in.nextInt();
                int upperBound = in.nextInt();
                System.out.println(permainan.lihat(lowerBound, upperBound));
                // permainan.printlist();
            } else if (query.equals("EVALUASI")) {
                //System.out.println(permainan.evaluasi((permainan.head)));
                permainan.insertionSort(permainan.head.next);
                permainan.printlist();

            } else {
                System.out.println("Query tidak valid!");
            } 
        }

        out.close();
    }

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