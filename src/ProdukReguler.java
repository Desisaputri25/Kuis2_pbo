public class ProdukReguler extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukReguler(String kode, String nama, int stok) {
        super(kode, nama, stok, 150000);
    }

    @Override
    public double hitungTotal(int jumlah) {
        return getHarga() * jumlah;
    }

    @Override
    public String getKategori() { return "Reguler"; }
}