package ai151.khimchenko;

/**
 * Накладная
 */
public class Invoice {

    private String madeBy;

    public String getMadeBy() {
        return madeBy;
    }

    public Invoice(String madeBy) {
        this.madeBy = madeBy;
    }
}
