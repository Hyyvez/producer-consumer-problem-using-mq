package model;

import java.io.Serializable;

public class ConsumerMessage implements Serializable {
    private int ConsumerID;

    // 构造函数
    public ConsumerMessage(int ConsumerID) {
        this.ConsumerID = ConsumerID;
    }

    // Getter 和 Setter 方法

    public int getConsumerID(){
        return ConsumerID;
    }
}
