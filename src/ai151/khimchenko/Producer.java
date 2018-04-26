package ai151.khimchenko;

import static java.lang.Thread.sleep;

public class Producer implements Runnable {

    Q q;
    Thread thread;
    int sleepDuration = 50;

    int id; //порядковый номер продюсера
    int maxQty; //максимально допустимое количество произведенных товаров
    int producedCtr; //текущее число произведенных товаров

    Producer(Q q, int id, int maxQty) {
        this.q = q;

        this.id = id;
        this.maxQty = maxQty;
        this.producedCtr = 0;

        this.thread = new Thread(this, "Продюсер " + id);
        this.thread.start();
    }

    public void run() {
        while (!q.madeMax() && producedCtr < maxQty) {
            try {
                if (q.put(new Item((100 * (id) + (producedCtr + 1)), id))) {
                    producedCtr++;
                }
                sleep(sleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        q.producerDone(String.valueOf(id));
    }

    String stats() {
        StringBuilder stats = new StringBuilder();
        stats.append("Продюсер ").append(id).append(" произвел ").append(producedCtr).append(" товаров из возможных ").append(maxQty).append(".");
        return stats.toString();
    }
}