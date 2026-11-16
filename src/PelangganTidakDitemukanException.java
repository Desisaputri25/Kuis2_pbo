public class PelangganTidakDitemukanException extends TokoException {
    public PelangganTidakDitemukanException(String id) {
        super("Pelanggan dengan ID " + id + " tidak ditemukan");
    }
}