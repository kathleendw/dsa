import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

public class LosPolosArmanos {
    private static InputReader in;
    private static PrintWriter out;
    public static int M;
    public static ArrayList<Map<String, Integer>> menu;
    public static int K;
    public static Map<Integer, ArrayList<String>> koki;
    public static int P;
    public static int C;
    public static int H;
    public static ArrayList<Integer> blackList = new ArrayList<Integer>();
    public static Queue<Integer> ruangLapar;
    public static Map<Integer, Integer> pelanggan;
    public static Map<Integer, Integer> pelangganMasuk;
    public static List<Map<Integer, ArrayList<Integer>>> urutanPemesanan = new ArrayList<Map<Integer, ArrayList<Integer>>>();
    public static Map<Integer, ArrayList<Integer>> sudahDilayani = new LinkedHashMap<Integer, ArrayList<Integer>>();
    public static ArrayList<String> barisPertama;
    public static ArrayList<String> barisSelanjutnya;
    public static ArrayList<String> status;
    public static ArrayList<ArrayList<String>> OUTPUT = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> fixedOutput = new ArrayList<ArrayList<String>>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        Scanner input = new Scanner(System.in); 

        M = in.nextInt();
        menu = new ArrayList<Map<String, Integer>>(); //key:tipe makanan, value:harga makanan
        Map<String, Integer> tipeHarga;
        for (int i = 0; i < M; i++) {
            int tipe = in.nextInt();
            String harga = in.next();  
            tipeHarga = new LinkedHashMap<String, Integer>();
            tipeHarga.put(harga, tipe);
            menu.add(tipeHarga);
        }
        
        K = in.nextInt();
        koki = new LinkedHashMap<Integer, ArrayList<String>>(); //key:idKoki, value:spesialisasi dan pelayanan
        int idKoki = 1;
        ArrayList<String> spesialisasiDanPelayanan;
        for (int i = 0; i < K; i++) {
            String spesialisasiKoki = in.next();
            spesialisasiDanPelayanan = new ArrayList<String>();
            spesialisasiDanPelayanan.add(spesialisasiKoki);
            spesialisasiDanPelayanan.add("0");
            koki.put(idKoki++, spesialisasiDanPelayanan);
        }

        P = in.nextInt();
        C = in.nextInt();
        H = in.nextInt();
        for (int i = 0; i < H; i++) {
            barisPertama = new ArrayList<String>();
            barisSelanjutnya = new ArrayList<String>();
            pelanggan = new LinkedHashMap<Integer, Integer>();
            status = new ArrayList<String>();
            ruangLapar = new LinkedList<Integer>();
            pelangganMasuk = new LinkedHashMap<Integer, Integer>();
            int jumlahPelanggan = in.nextInt();
            for (int j = 0; j < jumlahPelanggan; j++) {
                int idPelanggan = in.nextInt();
                String statusPelanggan = in.next();
                int uangPelanggan = in.nextInt();
                if (statusPelanggan.equals("?")) {
                    int rangeScanning = in.nextInt();
                }
                if (statusPelanggan.equals("+") || statusPelanggan.equals("-")) {
                    status.add(statusPelanggan);
                }
                pelanggan.put(idPelanggan, uangPelanggan);
                cekPelanggan(idPelanggan, statusPelanggan, uangPelanggan);
            }
            int jumlahPelayanan = in.nextInt();
            for (int j = 0; j < jumlahPelayanan; j++) {
                String query = in.next();
                if (query.equals("P")) {
                    int idPelanggan = in.nextInt();
                    int n = in.nextInt();
                    barisSelanjutnya.add(P(idPelanggan, n));
                }
                if (query.equals("L")) {
                    barisSelanjutnya.add(L());
                }
                else if (query.equals("B")) {
                    int idPelanggan = in.nextInt();
                    barisSelanjutnya.add(B(idPelanggan));
                }
                else if (query.equals("C")) {
                    int Q = in.nextInt();
                    barisSelanjutnya.add(C(Q));
                }
                else if (query.equals("D")) {
                    int costA = in.nextInt();
                    int costG = in.nextInt();
                    int costS = in.nextInt();
                    barisSelanjutnya.add(D(costA, costG, costS));
                }
            }
            OUTPUT.add(barisPertama);
            fixedOutput.add(barisSelanjutnya);
        }
        for (int i = 0; i < OUTPUT.size(); i++) {
            String pengeluaran = String.join(" ", OUTPUT.get(i));
            System.out.println(pengeluaran);
            String keluar = String.join("\n", fixedOutput.get(i));
            System.out.println(keluar);
        } 
        System.out.close();
    }

    public static void cekPelanggan(int idPelanggan, String statusPelanggan, int uangPelanggan) {
        int totalPositif = 0;
        int totalNegatif = 0;
        if (blackList.size() != 0) {
            for (int k = 0; k < blackList.size(); k++) {
                if (blackList.get(k) == idPelanggan) {
                    barisPertama.add("3");
                    break;
                }
                else if (statusPelanggan.equals("+")) {
                    barisPertama.add("0");
                    break;
                }
                else if (statusPelanggan.equals("?")) {
                    // advance scanning
                    for (int l = 0; l < status.size(); l++) {
                        if ((status.get(l)).equals("+")) {
                            totalPositif++;
                        }
                        else {
                            totalNegatif++;
                        }
                    }
                    if (totalPositif > totalNegatif) {
                        barisPertama.add("0");
                        status.add("+");
                    }
                    else {
                        status.add("-");
                        if (!(pelangganMasuk.size() < C)) {
                            ruangLapar.add(idPelanggan);
                            barisPertama.add("2");
                        }
                        else {
                            pelangganMasuk.put(idPelanggan, uangPelanggan);
                            barisPertama.add("1");
                        }
                    }
                    break;
                }
                else if (!(pelangganMasuk.size() < C)) {
                    ruangLapar.add(idPelanggan);
                    barisPertama.add("2");
                    break;
                }
                else {
                    pelangganMasuk.put(idPelanggan, uangPelanggan);
                    barisPertama.add("1");
                    break;
                }
            }
        }
        else {
            if (statusPelanggan.equals("+")) {
                barisPertama.add("0");
            }
            else if (statusPelanggan.equals("?")) {
                // advance scanning
                for (int l = 0; l < status.size(); l++) {
                    if ((status.get(l)).equals("+")) {
                        totalPositif++;
                    }
                    else {
                        totalNegatif++;
                    }
                }
                if (totalPositif > totalNegatif) {
                    barisPertama.add("0");
                    status.add("+");
                }
                else {
                    status.add("-");
                    if (!(pelangganMasuk.size() < C)) {
                        ruangLapar.add(idPelanggan);
                        barisPertama.add("2");
                    }
                    else {
                        pelangganMasuk.put(idPelanggan, uangPelanggan);
                        barisPertama.add("1");
                    }
                }
            }
            else if (!(pelangganMasuk.size() < C)) {
                ruangLapar.add(idPelanggan);
                barisPertama.add("2");
            }
            else {
                pelangganMasuk.put(idPelanggan, uangPelanggan);
                barisPertama.add("1");
            }
        }
    }

    public static String P(int idPelanggan, int n) {
        // pelanggan dengan ID idPelanggan akan memesan makanan ke-n di menu. Makanan ini harus dimasak oleh koki yang paling
        // sedikit melayani pelanggan dan sesuai spesialisasinya. Apabila terdapat koki dengan
        // spesialisasi yang sama memiliki jumlah pelayanan yang sama, maka koki dengan ID lebih kecil yang akan melayani.
        Map<String, Integer> pesanan = menu.get(n-1);
        ArrayList<Integer> pelangganDanKoki = new ArrayList<Integer>();
        pelangganDanKoki.add(idPelanggan);
        HashSet<Integer> x = new HashSet<Integer>();
        ArrayList<Integer> z = new ArrayList<Integer>();
        int min = 100000;
        for (Map.Entry<Integer, ArrayList<String>> e : koki.entrySet()) {
            for (Map.Entry<String, Integer> f : pesanan.entrySet()) {
                if (((e.getValue()).get(0)).equals(f.getKey())) { // jika spesialisasi koki sesuai dengan tipe pesanan
                    if (Integer.parseInt((e.getValue()).get(1)) < min) { // jika jumlah pelayanan lebih kecil dari min
                        min = Integer.parseInt((e.getValue()).get(1)); // untuk dapat yang jumlah pelayanannya paling sedikit
                        z.add(e.getKey()); //tambahkan idKoki di arraylist z
                    }
                }
            }
        }
        int result = z.get(z.size()-1);
        pelangganDanKoki.add(result);
        Map<Integer, ArrayList<Integer>> pemesanan = new LinkedHashMap<Integer, ArrayList<Integer>>();
        pemesanan.put(n, pelangganDanKoki);
        urutanPemesanan.add(pemesanan);
        return Integer.toString(result);
        // outputnya adalah id koki yg ambil pesanannya

        // menu = arraylist[map<str=int>], pesanan A = harga 50k
        // urutanPemesanan = arraylist[map<int=arraylist>] pesanan keberapa=[dipesan oleh idPelanggan 1, dimasak oleh idKoki 2];
        // koki = map<int=arraylist> idKoki = [spesialisasi A, jumlahpelayanan 0];
        // sudahDilayani = map<int=arraylist>, idPelanggan=[menu ke-n yg dipesan];
    }

    public static String L() {
        // pesanan yang sudah dipesan oleh pelanggan akan dilayani dan dimasak, kemudian makanan
        // akan dikirim ke pelanggan. Pesanan yang dilayani harus sesuai urutannya dengan urutan
        // pesanan tersebut. Setelah melayani, jumlah pelayanan yang dilakukan koki ditambah 1.
        Map<Integer, ArrayList<Integer>> i = urutanPemesanan.get(0);
        ArrayList<String> hah;
        int asd = 0;
        int pelangganDilayani = 0;
        for (Map.Entry<Integer, ArrayList<String>> e : koki.entrySet()) {
            for (Map.Entry<Integer, ArrayList<Integer>> f : i.entrySet()) {
                if (e.getKey() == (f.getValue()).get(1)) { // jika idKoki sesuai dengan idKoki yang mengambil pesanan
                    hah = new ArrayList<String>();
                    hah.add((e.getValue()).get(0)); //add spesialisasi koki
                    int y = (Integer.parseInt((e.getValue()).get(1))) + 1; //tambah jumlah pelayanan koki
                    hah.add(Integer.toString(y));
                    koki.put(e.getKey(), hah); //update koki
                }
                pelangganDilayani = (f.getValue()).get(0);
                asd = f.getKey(); // menu keberapa yang dilayani
            }
        }

        ArrayList<Integer> bla;
        ArrayList<Integer> pesananDilayani = new ArrayList<Integer>();
        if (sudahDilayani.size() == 0) { // jika belum ada pesanan yang dilayani
            pesananDilayani.add(asd);
            sudahDilayani.put(pelangganDilayani, pesananDilayani); //key:idPelanggan yang dilayani, value:pesanannya yang sudah dilayani
        }
        else { // jika sudah ada pesanan yang dilayani
            for (Map.Entry<Integer, ArrayList<Integer>> e : sudahDilayani.entrySet()) {
                if (e.getKey() != pelangganDilayani) { // jika pelanggan belum pernah dilayani
                    pesananDilayani.add(asd);
                    sudahDilayani.put(pelangganDilayani, pesananDilayani);
                    break;
                }
                else {
                    bla = e.getValue(); // get pesanan yang sudah dilayani
                    bla.add(asd); // tambahkan dengan pesanan yang barusan dilayani 
                    sudahDilayani.put(pelangganDilayani, bla); //update sudahdilayani
                    break;
                }
            }
        }
        urutanPemesanan.remove(0); //remove dari urutanpemesanan karena sudah dilayani
        return Integer.toString(pelangganDilayani);
        // outputnya adalah id pelanggan yg dilayani
    }

    public static String B(int idPelanggan) {
        // pelanggan dengan ID 𝐼𝐷_𝑃𝐸𝐿𝐴𝑁𝐺𝐺𝐴𝑁 harus membayar semua makanan yang telah dipesan. 
        // Jika pelanggan tidak mampu membayar, pelanggan ini akan di-blacklist untuk hari-hari berikutnya. 
        // Setelah melakukan pembayaran, pelanggan yang sedang menunggu di Ruang Lapar dapat masuk sesuai dengan urutan tunggu
        int totalPesanan = 0;
        ArrayList<Integer> menuKeN = new ArrayList<Integer>();
        for (Map.Entry<Integer, ArrayList<Integer>> e : sudahDilayani.entrySet()) { 
            if (e.getKey() == idPelanggan) { // jika idPelanggan sesuai dengan idPelanggan yang sudah pernah dilayani
                for (int i = 0; i < (e.getValue()).size(); i++) { // iterasi di arraylist yang berisi pesanan yang sudah dilayani
                    menuKeN.add((e.getValue()).get(i)); // pesanan yang sudah dilayani
                }
            }
        }
        ArrayList<Map<String, Integer>> k = new ArrayList<Map<String, Integer>>();
        for (int i = 0; i < menuKeN.size(); i++) {
            for (int j = 0; j < menu.size(); j++) { // [{A=50k}, {B=20k}]
                if (j == (menuKeN.get(i))-1) { // jika pesanan ke-n sesuai dengan menu
                    k.add(menu.get(j)); //add menu tersebut ke k
                }
            }
        }
        for (int i = 0; i < k.size(); i++) {
            for (Map.Entry<String, Integer> e : k.get(i).entrySet()) {
                totalPesanan += e.getValue(); //total yang harus dibayarkan pelanggan
            }
        }
        if (pelanggan.get(idPelanggan) >= totalPesanan) { //jika uangpelanggan cukup untuk membayar
            return "1";
        }
        else {
            blackList.add(idPelanggan); //jika tidak cukup, masuk blacklist
            return "0";
        }
    }

    public static String C(int Q) {
        // manajer restoran ingin mengetahui 𝑄 koki yang paling sedikit melayani pelanggan.
        // koki = map<int=arraylist> idKoki = [spesialisasi A, jumlahpelayanan 0]; {1=[S, 1], 2=[A, 0], 3=[G, 1], 4=[S, 0]}

        // membuat list yang berisi koki
        List<Map.Entry<Integer, ArrayList<String>>> list = new LinkedList<Map.Entry<Integer, ArrayList<String>>>(koki.entrySet());
        TreeSet<Map.Entry<Integer, ArrayList<String>>> set = new TreeSet<Map.Entry<Integer, ArrayList<String>>>(new descendingComparator());
        for (Map.Entry<Integer, ArrayList<String>> i : list) {
            set.add(i); // tambahkan map dari list ke set
        }
        ArrayList<String> arr = new ArrayList<String>();
        int i = 0;
        for (Map.Entry<Integer, ArrayList<String>> j : set) {
            if (i < Q) {
                arr.add(Integer.toString(j.getKey())); // tambahkan idKoki yang sudah disort berdasarkan jumlah pelayanannya
                i++;
            } 
        } 
        String hai = String.join(" ", arr);
        return hai;
    }

    public static String D(int costA, int costG, int costS) {
        // manajer restoran menawarkan penawaran menarik kepada pembeli yang ingin membeli tepat satu dari setiap menu yang ada. 
        // Selain membeli dengan harga normal, pembeli dapat membentuk Paket A, Paket G, dan Paket S dimana
        // masing-masing paket tersebut harus merupakan kumpulan makanan dari index i hingga index
        // j (dengan i < j) sesuai urutan makanan. Kumpulan makanan tersebut harus diawali dan
        // diakhiri dengan tipe makanan yang sama dengan tipe paket. Apabila suatu paket telah
        // terbentuk, harga masing-masing makanan pada paket tersebut mengikuti harga 𝐶𝑂𝑆𝑇_𝐴,
        // 𝐶𝑂𝑆𝑇_𝐺, atau 𝐶𝑂𝑆𝑇_𝑆 sesuai dengan tipenya. Setiap jenis paket hanya dapat dibentuk sekali,
        // sehingga jumlah maksimal paket yang dapat terbentuk adalah 3. Pelanggan perlu mencari
        // harga paling murah dengan menggabungkan harga menu satuan dengan harga paket yang ditawarkan.
        return "0";
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}

class descendingComparator implements Comparator<Map.Entry<Integer, ArrayList<String>>> {
    public int compare(Map.Entry<Integer, ArrayList<String>> o1, Map.Entry<Integer, ArrayList<String>> o2) {
        // Apabila terdapat koki yang memiliki jumlah pelayanan yang sama
        if (((o1.getValue()).get(1)).equals((o2.getValue()).get(1))) {
            // Jika koki tersebut memiliki spesialisasi yang sama
            if (((o1.getValue()).get(0)).equals((o2.getValue()).get(0))) {
                // maka urutkan berdasarkan ID yang terkecil
                return((o1.getKey()).compareTo(o2.getKey()));
            }
            // maka urutkan dari Spesialisasi Seafood, kemudian Groundfood, kemudian Airfood
            return(((o2.getValue()).get(0)).compareTo((o1.getValue()).get(0)));
        }
        // urutkan berdasarkan jumlah pelayanan koki dari kecil ke besar
        return(((o1.getValue()).get(1)).compareTo((o2.getValue()).get(1)));
    }
}
