public class ProdukPremium extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukPremium(String kode, String nama, int stok) {
        super(kode, nama, stok, 15000000);
    }

    @Override
    public double hitungTotal(int jumlah) {
        return getHarga() * jumlah * 1.02;
    }

    @Override
    public String getKategori() { return "Premium"; }
}
