package model;

import java.io.Serializable;

public class ProducerMessage implements Serializable {
    private int productID;

    // 构造函数
    public ProducerMessage(int productID) {
        this.productID = productID;
    }

    // Getter 和 Setter 方法

    public int getProductID() {
        return productID;
    }
}
