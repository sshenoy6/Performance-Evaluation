package disk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TwoThreadOneByteDisk implements Runnable {

	int position = 0;
	static Map<String,Double> random_read_latency = new HashMap<String,Double>();
	static Map<String,Double> random_write_latency = new HashMap<String,Double>();
	static Map<String,Double> sequential_read_latency = new HashMap<String,Double>();
	static Map<String,Double> sequential_write_latency = new HashMap<String,Double>();
	static Map<String,Double> random_read = new HashMap<String,Double>();
	static Map<String,Double> random_write = new HashMap<String,Double>();
	static Map<String,Double> sequential_read = new HashMap<String,Double>();
	static Map<String,Double> sequential_write = new HashMap<String,Double>();
	public TwoThreadOneByteDisk(int position){
		this.position = position;
	}
	public void run(){
		Path path_byte = Paths.get("SampleText.txt");
		
		ByteBuffer buffer_byte = ByteBuffer.allocate(10);
		
		try {
			FileChannel fileChannel_byte = FileChannel.open(path_byte);
			long startTime = System.nanoTime();
			//Random access for 1 Byte file
			int noOfBytesRead = fileChannel_byte.read(buffer_byte,position);
			int noOfBytesWritten = 0;
			buffer_byte.flip();
			double elapsed = (System.nanoTime() - startTime)/1000000000.0;
			
			random_read_latency.put(Thread.currentThread().getName(), (elapsed*1000));
			startTime =System.nanoTime();
			buffer_byte.get();
			random_write_latency.put(Thread.currentThread().getName(),(System.nanoTime() - startTime)/1000000.0);
			startTime = System.nanoTime();
			while(buffer_byte.hasRemaining()){
				buffer_byte.get();
				noOfBytesWritten++;
			}
			double elapsed_write = System.nanoTime() - startTime;
			double throughput_read = ((noOfBytesRead)/elapsed)/1048576.0;
			double throughput_write = ((noOfBytesWritten)* 1000000000/elapsed_write)/1048576.0;//total bytes/time taken
			
			random_read.put(Thread.currentThread().getName(), throughput_read);
			random_write.put(Thread.currentThread().getName(), throughput_write);
			fileChannel_byte.close();
			
			//Sequential access for 1 Byte file
			
			String filename = "SampleText1.txt";
	        FileOutputStream fos = new FileOutputStream (filename);
	        DataOutputStream dos = new DataOutputStream (fos);
	        long startWrite = System.nanoTime();
	        
	        dos.writeInt (42);
	        double latency = (System.nanoTime() - startWrite)/1000000.0;
	        dos.close();
	        fos.close();
	        double timeTaken = (System.nanoTime() - startWrite)/1000000000.0;
	        
	        startTime = System.nanoTime();
	        FileInputStream fis = new FileInputStream(filename);
	        DataInputStream dis = new DataInputStream(fis);
	        int value_File = dis.read();
	        dis.close();
	        fis.close();
	        
	        double elapsed_read = (System.nanoTime() - startTime)/1000000000.0;
	        throughput_write = (1.0/timeTaken)/1048576.0;
	        throughput_read = (1.0/elapsed_read)/1048576.0;
	     
	        
	        sequential_read.put(Thread.currentThread().getName(), throughput_read);
	        sequential_write.put(Thread.currentThread().getName(), throughput_write);
	        sequential_write_latency.put(Thread.currentThread().getName(), latency);
	        sequential_read_latency.put(Thread.currentThread().getName(), elapsed_read * 1000);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		
		Thread thread1 = new Thread(new TwoThreadOneByteDisk(1));
		thread1.setName("First Thread");
		Thread thread2 = new Thread(new TwoThreadOneByteDisk(2));
		thread2.setName("Second thread");
		
		thread1.start();
		thread2.start();
		
		
		thread1.join();
		thread2.join();
		//Average latency for random and sequential operations
		double avg_random_write_latency =0.0f,avg_random_read_latency=0.0f,avg_sequential_read_latency=0.0f,avg_sequential_write_latency =0.0f,avg_random_read =0.0f,avg_random_write =0.0f,avg_sequential_read=0.0f,avg_sequential_write=0.0f;
		if(!thread1.isAlive() && !thread2.isAlive()){
			avg_random_read_latency = (random_read_latency.get("First Thread") + random_read_latency.get("Second thread"))/2.0;
			avg_random_write_latency = (random_write_latency.get("First Thread") + random_write_latency.get("Second thread"))/2.0;
			avg_sequential_write_latency = (sequential_write_latency.get("First Thread") + sequential_write_latency.get("Second thread"))/2.0;
			avg_sequential_read_latency = (sequential_read_latency.get("First Thread") + sequential_read_latency.get("Second thread"))/2.0;
			avg_random_read = (random_read.get("First Thread") + random_read.get("Second thread"))/2.0;
			avg_sequential_read = (sequential_read.get("First Thread") + sequential_read.get("Second thread"))/2.0;
			avg_random_write = (random_write.get("First Thread") + random_write.get("Second thread"))/2.0;
			avg_sequential_write = (sequential_write.get("First Thread") + sequential_write.get("Second thread"))/2.0;
			System.out.println("----------For 1 byte data-----------");
			System.out.println("The value of latency for two threads random read operation is "+avg_random_read_latency);
			System.out.println("The value of latency for two threads random write operation is "+avg_random_write_latency);
			System.out.println("The value of latency for two threads sequential read operation is "+avg_sequential_read_latency);
			System.out.println("The value of latency for two threads sequential write operation is "+avg_sequential_write_latency);
			System.out.println("The value of throughput for two threads random read is "+avg_random_read);
			System.out.println("The value of throughput for two threads sequential read is "+avg_sequential_read);
			System.out.println("The value of throughput for two threads random write is "+avg_random_write);
			System.out.println("The value of throughput for two threads sequential write is "+avg_sequential_write);
		}
	}
}
