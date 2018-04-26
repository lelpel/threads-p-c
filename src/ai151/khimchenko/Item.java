package ai151.khimchenko;

public class Item {

    private int id;
    private int producerId;

    int getId() {
        return id;
    }

    int getProducerId() {
        return producerId;
    }

    Item(int id, int producerId) {
        this.id = id;
        this.producerId = producerId;
    }
}
