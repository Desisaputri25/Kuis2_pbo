public class Pelanggan extends Aktor {
    private static final long serialVersionUID = 1L;

    private double totalBelanja = 0;

    public Pelanggan(String id, String nama) {
        super(id, nama);
    }

    public double getTotalBelanja() { return totalBelanja; }

    public void tambahBelanja(double nominal) {
        this.totalBelanja += nominal;
    }

    @Override
    public String getPeran() { return "Pelanggan"; }
}