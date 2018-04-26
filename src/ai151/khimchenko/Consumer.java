package ai151.khimchenko;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Consumer implements Runnable {

    static final int HIGH_PRIORITY = 4;
    static final int MEDIUM_PRIORITY = 2;
    static final int LOW_PRIORITY = 1;

    Q q;
    Thread thread;
    int sleepDuration = 250;

    String name;
    int priority;
    ArrayList<Item> itemsReceived;

    Consumer(Q q, String name, int priority) {
        this.q = q;

        this.name = name;
        this.priority = priority;
        itemsReceived = new ArrayList<>();

        this.thread = new Thread(this, "Консьюмер " + this.name);
        this.thread.start();
    }

    public void run() {
        while (!q.invoicesMax()) {
            int iterations = getAvailableInvoicesCnt();

            for (int i = 0; i < iterations; i++) {
                try {
                    Item item = q.get(new Invoice(name));
                    if (item != null)
                        itemsReceived.add(item);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                sleep(sleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        q.consumerDone(name);
    }

    public int getAvailableInvoicesCnt() {
        int count;
        if (priority <= q.leftInvoices())
            count = priority;
        else
            count = q.leftInvoices();
        return count;
    }

    public String stats() {
        StringBuilder stats = new StringBuilder();
        stats.append("Консьюмер ").
                append(name).append(" успешно получил ").
                append(itemsReceived.size()).
                append(" товаров.\n");

        for(int i = 1; i <= 3; i++) {
            final int index=i;
            long count = itemsReceived.stream().filter(item -> item.getProducerId() == index).count();
            stats.append(">>").append(count).append(" товаров от продюсера №").append(i).append(";\n");
        }
        return  stats.toString();
    }
}
