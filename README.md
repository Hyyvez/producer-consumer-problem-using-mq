# producer-consumer-problem-using-mq
一个用简单的队列解决生产者消费者问题的玩具程序


### What
是操作系统的大作业😅

基于消息队列实现生产者—消费者问题
目前的计算机系统均提供了多任务并行环境．无论是应用程序还是系统程序．都需要针对每一个任务创建相应的进程。进程是设计和分析操作系统的有力工具。然而不同的进程之间．即使是具有家族联系的父子进程．都具有各自不同的进程映像。由于不同的进程运行在各自不同的内存空间中．一方对于变量的修改另一方是无法感知的．因此．进程之间的信息传递不可能通过变量或其它数据结构直接进行，只能通过进程间通信来完成。
消息队列是由消息的链表，存放在内核中并由消息队列标识符标识。消息队列克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点。

【设计要求】  
1.编写程序实现一个生产者与多个消费者。  
Write a program to implement a producer and multiple consumers.

2.生产者与消费者以随机的方式对仓库进行访问。  
The producer and consumers access the warehouse in a random manner.

3.仓库满的时候，生产者不能进入仓库，仓库空的时候，消费者不能进入仓库消费产品。  
When the warehouse is full, the producer cannot enter the warehouse, and when the warehouse is empty, the consumers cannot enter the warehouse to consume products.

4.生产者自动生产产品。  
The producer automatically produces products.

5.生产者与消费者处于不同进程  
The producer and consumers are in different processes.

6.两个进程间使用socket通信  
The communication between the two processes is done using sockets.

### How
先运行 warehouse ，然后再运行 producer 和 consumer （这两者不分顺序）  
First, run the Warehouse program, and then run the Producer and Consumer (the order of running these two doesn't matter).
