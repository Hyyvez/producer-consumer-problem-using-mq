import model.ConsumerMessage;
import model.ProducerMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Warehouse extends LinkedList<String> {
    static BlockingQueue<Integer> warehouseproductsQueue = new LinkedBlockingQueue<Integer>();
    static Queue<ProducerMessage> producerProductsPending = new LinkedList<ProducerMessage>();
    static Queue<ConsumerMessage> consumerProductsPending = new LinkedList<ConsumerMessage>();
    int warehouseCapacity = 12;//修改仓库大小

    public static void main(String[] arg) {
        Warehouse wh = new Warehouse();
        wh.receiveSocketThread(12345);
    }

    public void receiveSocketThread(int port) {
        try {
            // 创建ServerSocket对象并监听指定端口
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("socket 已创建...");

            while (true) {
                // 接受（客户端）消费者和生产者连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("1 个（客户端）消费者或生产者已连接" + clientSocket.getInetAddress());

                // 创建新线程处理客户端连接
                Thread thread = new Thread(new Warehouse.ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // 创建输入流
                InputStream inputStream = clientSocket.getInputStream();

                // 持续等待接收客户端的消息
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                while (true) {
                    Object receivedObject = objectInputStream.readObject();

                    if (receivedObject instanceof ProducerMessage) {
                        ProducerMessage pMessage = (ProducerMessage) receivedObject;
                        int productID = pMessage.getProductID();
                        System.out.println("收到生产者生产的产品， 产品ID: " + productID);
                        //仓库未满，放入仓库
                        if (warehouseproductsQueue.size() < warehouseCapacity) {
                            //在这可运行生产者任务的代码
                            warehouseproductsQueue.offer(productID);

                        } else {
                            //放入生产者等待队列
                            producerProductsPending.offer(pMessage);
                            System.out.println("仓库已满，产品 " + productID + ", 加入生产者产品队列等待。生产者产品队列长度：" + producerProductsPending.size());

                        }
                        updater(); // 更新操作
                    } else if (receivedObject instanceof ConsumerMessage) {
                        ConsumerMessage cMessage = (ConsumerMessage) receivedObject;
                        int consumerID = cMessage.getConsumerID();
                        String productID = String.valueOf(warehouseproductsQueue.peek());

                        System.out.println("--------------------" + "消费者 " + consumerID + " 尝试消费产品");
                        //仓库有产品，消费产品
                        if (warehouseproductsQueue.size() != 0) {
                            //在这可运行消费者任务的代码

                            warehouseproductsQueue.poll();

                            System.out.println("--------------------" + "消费者 " + consumerID + " 消费了产品 " + productID);
                        } else {
                            //放入消费者等待队列
                            consumerProductsPending.offer(cMessage);
                            System.out.println("--------------------" + "消费者 " + consumerID + " 消费产品失败, 仓库为空. 加入消费者产品队列等待。消费者产品队列长度：" + consumerProductsPending.size());
                        }
                        updater(); // 更新操作
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        public void updater() throws IOException {
            // 执行更新操作
            // 检查仓库是否有空位，如果有空位则从生产者队列取出产品放入仓库

            while (warehouseproductsQueue.size() < warehouseCapacity && !producerProductsPending.isEmpty()) {
                ProducerMessage pMessage = producerProductsPending.poll();
                int productID = pMessage.getProductID();
                warehouseproductsQueue.offer(productID);
                System.out.println("----------------------------------------" + "将产品 " + productID + "从生产者产品队列放到仓库，生产者产品队列长度：" + producerProductsPending.size());
            }

            // 检查仓库是否有产品，如果有产品则从消费者队列取出消费者并消耗产品
            while (warehouseproductsQueue.size() != 0 && !consumerProductsPending.isEmpty()) {

                ConsumerMessage cMessage = consumerProductsPending.poll();
                int consumerID = cMessage.getConsumerID();
                String productID = String.valueOf(warehouseproductsQueue.poll());
                System.out.println("----------------------------------------" + "消费者" + consumerID + "消费了从仓库取出的产品 " + productID + "。消费者产品队列长度:" + consumerProductsPending.size());
            }
        }
    }
}
