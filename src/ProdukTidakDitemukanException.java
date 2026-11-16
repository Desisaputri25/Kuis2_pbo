public class ProdukTidakDitemukanException extends TokoException {
    public ProdukTidakDitemukanException(String kode) {
        super("Produk dengan kode " + kode + " tidak ditemukan");
    }
}