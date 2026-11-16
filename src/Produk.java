import java.io.Serializable;

public abstract class Produk implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kode;
    private String nama;
    private int stok;
    private double harga;

    public Produk(String kode, String nama, int stok, double harga) {
        this.kode = kode;
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getStok() { return stok; }
    public double getHarga() { return harga; }

    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah > stok) {
            throw new StokTidakCukupException(
                "Stok " + nama + " (" + kode + ") tidak cukup. Diminta: " + jumlah + ", tersedia: " + stok);
        }
        stok -= jumlah;
    }

    public abstract double hitungTotal(int jumlah);
    public abstract String getKategori();

    @Override
    public String toString() {
        return kode + " - " + nama + " (" + getKategori() + "), stok: " + stok;
    }
}