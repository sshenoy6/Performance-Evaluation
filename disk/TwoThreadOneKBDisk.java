package disk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class TwoThreadOneKBDisk implements Runnable {

	int position = 0;
	int FILE_SIZE_KBYTE = 500 * 1024;
	int PAGE_SIZE = 4096;
	byte[] BLANK_ARRAY = new byte[1024];
	static Map<String,Double> random_read_latency = new HashMap<String,Double>();
	static Map<String,Double> sequential_read_latency = new HashMap<String,Double>();
	static Map<String,Double> random_write_latency = new HashMap<String,Double>();
	static Map<String,Double> sequential_write_latency = new HashMap<String,Double>();
	static Map<String,Double> random_read = new HashMap<String,Double>();
	static Map<String,Double> random_write = new HashMap<String,Double>();
	static Map<String,Double> sequential_read = new HashMap<String,Double>();
	static Map<String,Double> sequential_write = new HashMap<String,Double>();


	public TwoThreadOneKBDisk(int position) {
		this.position = position;
	}

	public void run() {
		
		byte[] kbData = new byte[4096];
		
		//Random file operations
		RandomAccessFile randomAccessFile_KB = null;
		try {
			randomAccessFile_KB = new RandomAccessFile("SampleText.txt", "rw");
			long startWrite = System.nanoTime();
			randomAccessFile_KB.write(1);
			randomAccessFile_KB.write(2);
			randomAccessFile_KB.write(3);
			randomAccessFile_KB.write(4);
			double latency = (System.nanoTime() - startWrite)/4;
			// random access read write 1 Kbyte
			startWrite = System.nanoTime();
			 for (long i = 0; i < 200; i++) {
				randomAccessFile_KB.write(kbData, 5, 1024);
			 }
			long endWrite = System.nanoTime();
			double timeTaken = (endWrite - startWrite);
			double throughput_write = (1024.0 *200 * 1000000000/ timeTaken) / 1048576.0;//total bytes/time taken

			long startRead = System.nanoTime();
			
			randomAccessFile_KB.read(kbData,56,1024);
			double index =  Math.random() * 10;
			  for (long i = 0; i < 250; i++) {
				  randomAccessFile_KB.read(kbData,(int)index,1024); 
			 }
			 
			long endRead = System.nanoTime();
			timeTaken = (endRead - startRead);
			double throughput_read = (1024.0 * 251 * 1000000000/ timeTaken) / 1048576.0;
			
			//store the values into hash map to calculate average
			random_read.put(Thread.currentThread().getName(), throughput_read);
			random_write.put(Thread.currentThread().getName(), throughput_write);
			random_write_latency.put(Thread.currentThread().getName(), (endWrite - startWrite)/200.0);
			random_read_latency.put(Thread.currentThread().getName(),(endRead - startRead)/250.0 );
			
			
			// sequential access for 1KByte file
			String filename = "SampleText2.txt";
			FileOutputStream fos = new FileOutputStream(filename);
			DataOutputStream dos = new DataOutputStream(fos);
			startWrite = System.nanoTime();
			dos.writeInt(62);
			latency = (System.nanoTime() - startWrite);
			dos.close();
			fos.close();

			startWrite = System.nanoTime();
			FileOutputStream out = new FileOutputStream(filename);
			PrintStream pout = new PrintStream(out);
			for (long i = 0; i < 400; i++) {
				pout.write(kbData);
			}
			pout.close();
			out.close();
			endWrite = System.nanoTime();
			timeTaken = (endWrite - startWrite)/ 1000000000.0;
			throughput_write = (1024.0 * 400/timeTaken)/1048576.0;
			
			startRead = System.nanoTime();
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream dis = new DataInputStream(fis);
			dis.read(BLANK_ARRAY);
			for (long i = 0; i < 20; i++) {
				dis.read(BLANK_ARRAY);
			}
			dis.close();
			fis.close();
			endRead = System.nanoTime();

			timeTaken = (endRead - startRead) / 1000000000.0;
			throughput_read = (1024.0 * 21/timeTaken) /1048576.0;
			
			sequential_read.put(Thread.currentThread().getName(), throughput_read);
			sequential_write.put(Thread.currentThread().getName(), throughput_write);
			sequential_read_latency.put(Thread.currentThread().getName(), (endRead - startRead)/21.0);
			sequential_write_latency.put(Thread.currentThread().getName(), (endWrite - startWrite)/400.0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {

		Thread thread1 = new Thread(new TwoThreadOneKBDisk(270));
		thread1.setName("First Thread");
		Thread thread2 = new Thread(new TwoThreadOneKBDisk(478));
		thread2.setName("Second Thread");
		
		thread1.start();
		thread2.start();
		
		thread1.join();
		thread2.join();
		
		//calculate average latency and throughput over two threads
		double avg_random_read_latency =0.0f,avg_random_write_latency =0.0f,avg_sequential_read_latency =0.0f,avg_sequential_write_latency =0.0f,avg_random_read =0.0f,avg_random_write =0.0f,avg_sequential_read=0.0f,avg_sequential_write=0.0f;
		if(!thread1.isAlive() && !thread2.isAlive()){
			avg_random_read_latency = (random_read_latency.get("First Thread") + random_read_latency.get("Second Thread"))/2.0;
			avg_random_write_latency = (random_write_latency.get("First Thread") + random_write_latency.get("Second Thread"))/2.0;
			avg_sequential_read_latency = (sequential_read_latency.get("First Thread") + sequential_read_latency.get("Second Thread"))/2.0;
			avg_sequential_write_latency = (sequential_write_latency.get("First Thread") + sequential_write_latency.get("Second Thread"))/2.0;
			avg_random_read = (random_read.get("First Thread") + random_read.get("Second Thread"))/2.0;
			avg_sequential_read = (sequential_read.get("First Thread") + sequential_read.get("Second Thread"))/2.0;
			avg_random_write = (random_write.get("First Thread") + random_write.get("Second Thread"))/2.0;
			avg_sequential_write = (sequential_write.get("First Thread") + sequential_write.get("Second Thread"))/2.0;
			System.out.println("----------For 1 Kbyte data-----------");
			System.out.println("The value of latency for two threads random read operation in ms is "+(avg_random_read_latency)/1000);
			System.out.println("The value of latency for two threads random write operation in ms is "+(avg_random_write_latency)/1000);
			System.out.println("The value of latency for two threads sequential read operation in ms is "+(avg_sequential_read_latency)/1000);
			System.out.println("The value of latency for two threads sequential write operation in ms is "+avg_sequential_write_latency/1000);
			System.out.println("The value of throughput for two threads random read in MB/s is "+avg_random_read);
			System.out.println("The value of throughput for two threads sequential read in MB/s is "+avg_sequential_read);
			System.out.println("The value of throughput for two threads random write in MB/s is "+avg_random_write);
			System.out.println("The value of throughput for two threads sequential write in MB/s is "+avg_sequential_write);
		}
	}
}
