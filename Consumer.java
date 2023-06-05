import model.ConsumerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Consumer {
    private static final int serverPort = 12345; // Warehouse服务器端口号
    private static final String serverIp = "127.0.0.1"; // Warehouse服务器 IP 地址
    private static final int perSec = 50; // 平均每perSec秒发送一个消息

    public static void main(String[] args) {
        int numConsumers = 5; // 指定消费者线程数量

        for (int i = 0; i < numConsumers; i++) {
            Thread thread = new Thread(() -> {
                try (Socket socket = new Socket(serverIp, serverPort);
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

                    int massageID = Integer.parseInt(
                            Thread.currentThread().getName()
                                    .substring("consumer".length()));
                    while (true) {
                        // 创建 ConsumerMessage 对象
                        ConsumerMessage cMessage = new ConsumerMessage(massageID);

                        // 发送 ConsumerMessage 对象给 Warehouse
                        oos.writeObject(cMessage);
                        oos.flush();

                        System.out.println("Consumer " + Thread.currentThread().getName() + " sent a request.");

                        // 等待一段时间
                        int interval = (int) (perSec * Math.random() * 2);
                        Thread.sleep(interval * 1000L);

                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });

            thread.setName("Consumer" + i); // 设置消费者线程名称
            thread.start();
        }
    }
}
