import model.ProducerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Producer {
    private int productId = 0;
    static int perSec = 10;         // 平均每perSec秒发送一个消息

    private static final int serverPort = 12345; // Warehouse服务器端口号
    private static final String serverIp = "127.0.0.1"; // Warehouse服务器 IP 地址

    public static void main(String[] args) {
        Producer producer = new Producer();

        try (Socket socket = new Socket(serverIp, serverPort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            while (true) {
                producer.produce(oos);
                int interval = (int) (perSec * Math.random() * 2);
                Thread.sleep(interval * 1000L);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void produce(ObjectOutputStream oos) throws IOException {
        // 创建 ProducerMessage 对象
        ProducerMessage pMessage = new ProducerMessage(productId);

        // 发送 ProducerMessage 对象给 Warehouse
        oos.writeObject(pMessage);
        oos.flush();

        System.out.println("Produced a product, Product ID: " + productId);
        productId++;
    }
}
