public class ProdukEkonomis extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukEkonomis(String kode, String nama, int stok) {
        super(kode, nama, stok, 5000);
    }

    @Override
    public double hitungTotal(int jumlah) {
        double total = getHarga() * jumlah;
        return jumlah >= 10 ? total * 0.95 : total;
    }

    @Override
    public String getKategori() { return "Ekonomis"; }
}