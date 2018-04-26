package ai151.khimchenko;

public class Main {

    static int maxItems = 100;
    static int qty = 40;
    static int consumers = 4;

    private static Q q;

    public static void main(String args[]) {
        q = new Q(maxItems, consumers);

        Producer[] producers = new Producer[] {
                new Producer(q, 1, qty),
                new Producer(q, 2, qty),
                new Producer(q, 3, qty)
        };

        Consumer[] consumers = new Consumer[] {
                new Consumer(q, "Ветеран ВОВ", Consumer.HIGH_PRIORITY),
                new Consumer(q, "Служивый", Consumer.MEDIUM_PRIORITY),
                new Consumer(q, "Гражданин", Consumer.LOW_PRIORITY),
                new Consumer(q, "Гражданочка", Consumer.LOW_PRIORITY)
        };

        try {
            for (Producer producer : producers) {
                producer.thread.join();
            }

            for (Consumer consumer : consumers) {
                consumer.thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stats(producers, consumers);
    }

    private static void stats(Producer[] producers, Consumer[] consumers) {
        System.out.println("\n\n-----------СТАТИСТИКА---------------------");
        System.out.println("Накладных произведено: " + q.getQtyMadeItems());
        System.out.println("Накладных поставлено: " + q.getQtyShippedItems() + "\n");

        for (Producer producer : producers) {
            System.out.println(producer.stats());
        }
        System.out.println("");
        for (Consumer consumer : consumers) {
            System.out.println(consumer.stats());
        }
    }
}
