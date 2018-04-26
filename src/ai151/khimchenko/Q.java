package ai151.khimchenko;

import java.util.LinkedList;
import java.util.Queue;

public class Q {

    private int maxItems;
    private int qty;

    private Queue<Invoice> invoices = new LinkedList<>();
    private Item lockedItem;
    private boolean itemAvailable = false;

    private Object lockGet = new Object();
    private Object lockPut = new Object();

    private int invoicesMade = 0;
    private int itemsMade = 0;
    private int itemsShipped = 0;

    Q(int maxItems, int qty) {
        this.maxItems = maxItems;
        this.qty = qty;
    }

    Item get(Invoice invoice) throws InterruptedException {
        String name = invoice.getMadeBy();
        synchronized (lockGet) {
            if (invoicesMax()) {
                lockGet.notifyAll();
                return null;
            }
            if (invoices.size() == qty) {
                lockGet.wait();
            }
            invoices.add(invoice);
            invoicesMade++;
            System.out.println("Консьюмер" + name + " положил накладную.");
            lockGet.notifyAll();
        }
        synchronized (lockPut) {
            while (!itemAvailable && !shippedMax()) {
                lockPut.wait();
            }
            if (itemAvailable && !shippedMax()) {
                Item item = lockedItem;
                System.out.println("Коннсьюмер  " + name + " получил продукт №" + item.getId());
                itemsShipped++;
                lockedItem = null;
                itemAvailable = false;
                lockPut.notifyAll();
                return item;
            } else return null;
        }
    }

    boolean put(Item item) throws InterruptedException {
        int source = item.getProducerId();

        synchronized (lockGet) {
            if (invoices.size() == 0) {
                if (madeMax()) {
                    lockGet.notifyAll();
                    return false;
                }
                lockGet.wait();
            }
            synchronized (lockPut) {
                if (invoices.size() > 0 && !itemAvailable) {
                    lockedItem = item;
                    itemAvailable = true;
                    System.out.println("Продюсер " + source + " успешно произвел продукт " + item.getId());
                    invoices.poll();
                } else {
                    return false;
                }
                itemsMade++;
                lockPut.notifyAll();
                return true;
            }
        }
    }

    public int getQtyMadeItems() {
        return invoicesMade;
    }

    public int getQtyShippedItems() {
        return itemsMade;
    }

    synchronized void producerDone(String name) {
        System.out.println("Продюсер " + name + " успещно завершил работу.");
        notifyAll();
    }

    synchronized void consumerDone(String name) {
        System.out.println("Консьюмер " + name + " успещно завершил работу.");
        notifyAll();
    }

    /*
     * Данные методы определяют, выполнены ли максимальные квоты на производство накладных, товаров и получение
     */
    synchronized boolean invoicesMax() {
        return maxItems == invoicesMade;
    }

    synchronized boolean madeMax() {
        return maxItems == itemsMade;
    }

    synchronized boolean shippedMax() {
        return maxItems == itemsShipped;
    }

    synchronized int leftInvoices() {
        return maxItems - invoicesMade;
    }

}

