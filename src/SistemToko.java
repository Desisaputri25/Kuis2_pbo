import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemToko implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaToko;
    private HashMap<String, Pelanggan> daftarPelanggan = new HashMap<>();
    private ArrayList<Produk> daftarProduk = new ArrayList<>();
    private ArrayList<Transaksi> logTransaksi = new ArrayList<>();

    public SistemToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public void tambahPelanggan(String id, String nama) {
        daftarPelanggan.put(id, new Pelanggan(id, nama));
    }

    public void tambahProduk(String kode, String nama, int stok) {
        Produk p;
        if (nama.contains("Premium")) {
            p = new ProdukPremium(kode, nama, stok);
        } else if (nama.contains("Ekonomis")) {
            p = new ProdukEkonomis(kode, nama, stok);
        } else {
            p = new ProdukReguler(kode, nama, stok);
        }
        daftarProduk.add(p);
    }

    private Pelanggan cariPelanggan(String id) throws PelangganTidakDitemukanException {
        Pelanggan p = daftarPelanggan.get(id);
        if (p == null) throw new PelangganTidakDitemukanException(id);
        return p;
    }

    private Produk cariProduk(String kode) throws ProdukTidakDitemukanException {
        for (Produk p : daftarProduk) {
            if (p.getKode().equals(kode)) return p;
        }
        throw new ProdukTidakDitemukanException(kode);
    }

    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        StringBuilder isi = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                isi.append(baris).append(" ");
            }
        }

        String[] daftarRecord = isi.toString().trim().split("\\s+");

        String formatBaris = "%-3s %-9s %-13s %-12s %-4s %s%n";
        System.out.printf(formatBaris, "No", "Status", "ID Pelanggan", "Kode Produk", "Jml", "Keterangan");
        System.out.printf(formatBaris, "--", "--------", "------------", "-----------", "---", "----------");

        int nomor = 0, berhasil = 0, gagal = 0;
        for (String record : daftarRecord) {
            if (record.isEmpty()) continue;
            nomor++;
            String id = "-", kd = "-", jml = "-";
            try {
                HashMap<String, String> data = parseRecord(record);
                id = data.get("ID");
                kd = data.get("KD");
                jml = data.get("JML");
                Transaksi t = prosesData(data);
                berhasil++;
                System.out.printf(formatBaris, nomor, "BERHASIL", id, kd, jml,
                    "Stok " + kd + " tersisa " + t.getProduk().getStok());
            } catch (TokoException e) {
                gagal++;
                System.out.printf(formatBaris, nomor, "GAGAL", id, kd, jml, e.getMessage());
            }
        }

        System.out.println();
        System.out.println("Ringkasan: " + nomor + " transaksi | " + berhasil + " berhasil | " + gagal + " gagal");
        System.out.println();
        System.out.println("Sisa stok akhir:");
        for (Produk p : daftarProduk) {
            System.out.printf("  %-7s %-18s %d%n", p.getKode(), p.getNama(), p.getStok());
        }
        System.out.println();
    }

    private HashMap<String, String> parseRecord(String record) throws FormatDataException {
        HashMap<String, String> data = new HashMap<>();
        for (String bagian : record.split(",")) {
            if (bagian.isEmpty()) continue;
            String[] kv = bagian.split("=", 2);
            if (kv.length != 2) {
                throw new FormatDataException("Format salah pada bagian '" + bagian + "'");
            }
            data.put(kv[0].trim(), kv[1].trim());
        }
        if (!data.containsKey("ID") || !data.containsKey("KD") || !data.containsKey("JML")) {
            throw new FormatDataException("Field ID/KD/JML tidak lengkap");
        }
        return data;
    }

    private Transaksi prosesData(HashMap<String, String> data) throws TokoException {
        int jumlah;
        try {
            jumlah = Integer.parseInt(data.get("JML"));
        } catch (NumberFormatException e) {
            throw new FormatDataException("JML bukan angka: " + data.get("JML"));
        }
        if (jumlah <= 0) {
            throw new FormatDataException("JML harus lebih dari 0");
        }

        Pelanggan pelanggan = cariPelanggan(data.get("ID"));
        Produk produk = cariProduk(data.get("KD"));

        produk.kurangiStok(jumlah);
        Transaksi t = new Transaksi(pelanggan, produk, jumlah);
        pelanggan.tambahBelanja(t.getTotal());
        logTransaksi.add(t);
        return t;
    }

    public void simpanLaporanAkhir() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_master.bak"))) {
            oos.writeObject(this);
            System.out.println("[INFO] Serialisasi berhasil -> data_master.bak");
        } catch (IOException e) {
            System.out.println("[ERROR] Serialisasi gagal: " + e.getMessage());
        }

        try (FileWriter fw = new FileWriter("laporan.txt")) {
            for (Transaksi t : logTransaksi) {
                fw.write(t.toCsv() + "\n");
            }
            System.out.println("[INFO] Laporan CSV berhasil -> laporan.txt");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis laporan: " + e.getMessage());
        }
    }
}