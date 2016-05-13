package disk;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class TwoThreadOneMBDisk implements Runnable {

	int position = 0;
	int FILE_SIZE_MBYTE = 500 * 1024 * 1024;
	int PAGE_SIZE = 4096;
	byte[] BLANK_PAGE = new byte[PAGE_SIZE];
	static Map<String,Double> random_read_latency = new HashMap<String,Double>();
	static Map<String,Double> sequential_read_latency = new HashMap<String,Double>();
	static Map<String,Double> random_write_latency = new HashMap<String,Double>();
	static Map<String,Double> sequential_write_latency = new HashMap<String,Double>();
	static Map<String,Double> random_read = new HashMap<String,Double>();
	static Map<String,Double> random_write = new HashMap<String,Double>();
	static Map<String,Double> sequential_read = new HashMap<String,Double>();
	static Map<String,Double> sequential_write = new HashMap<String,Double>();

	public TwoThreadOneMBDisk(int position) {
		this.position = position;
	}

	public void run() {

		byte[] mbData = new byte[10485770];
		byte[] BLANK_ARRAY = new byte[1048576]; //create array of 1 MB size to read and write
		RandomAccessFile randomAccessFile_MB = null;
		try {
			randomAccessFile_MB = new RandomAccessFile("SampleText.txt", "rw");
			long startWrite = System.nanoTime();
			randomAccessFile_MB.write(1);
			randomAccessFile_MB.write(2);
			randomAccessFile_MB.write(3);
			randomAccessFile_MB.write(4);
			double latency = (System.nanoTime() - startWrite)/4;
			// random access read write 1 Mbyte
			startWrite = System.nanoTime();
			for (long i = 0; i < 5; i++) {
				randomAccessFile_MB.write(mbData, 5, 1048576);
			}
			long endWrite = System.nanoTime();
			double timeTaken = (endWrite - startWrite);
			double throughput_write = (5.0 * 1000000000/ timeTaken);//total bytes/time taken

			long startRead = System.nanoTime();
			
			randomAccessFile_MB.read(mbData,56,1048576);
			
			double index =  Math.random() * 10;
			for (long i = 0; i < 25; i++) {
				randomAccessFile_MB.read(mbData,(int)index,1048576); 
			}

			long endRead = System.nanoTime();
			timeTaken = (endRead - startRead);
			double throughput_read = (25.0 * 1000000000/ timeTaken);//throughput to read 1 MB 25 times
			
			random_read.put(Thread.currentThread().getName(), throughput_read); //add the values to Hashmaps to calculate average over two threads
			random_write.put(Thread.currentThread().getName(), throughput_write);
			random_write_latency.put(Thread.currentThread().getName(), (endWrite - startWrite)/5.0);//latency per MB written
			random_read_latency.put(Thread.currentThread().getName(),(endRead - startRead)/25.0 );//latency per MB read


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
			BufferedOutputStream buff = new BufferedOutputStream(out);
			PrintStream pout = new PrintStream(buff);
			for (long i = 0; i < 30; i++) {
				pout.write(BLANK_ARRAY);
			}
			pout.close();
			out.close();
			endWrite = System.nanoTime();
			timeTaken = (endWrite - startWrite)/ 1000000000.0;
			throughput_write = (30.0/timeTaken);

			startRead = System.nanoTime();
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream dis = new DataInputStream(fis);
			dis.readFully(BLANK_ARRAY);
			for (long i = 0; i < 20; i++) {
				dis.readFully(BLANK_ARRAY);
			}
			dis.close();
			fis.close();
			endRead = System.nanoTime();

			timeTaken = (endRead - startRead) / 1000000000.0;
			throughput_read = (21.0/timeTaken);
			
			sequential_read.put(Thread.currentThread().getName(), throughput_read);
			sequential_write.put(Thread.currentThread().getName(), throughput_write);
			sequential_read_latency.put(Thread.currentThread().getName(), (endRead - startRead)/21.0);
			sequential_write_latency.put(Thread.currentThread().getName(), (endWrite - startWrite)/30.0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {

		Thread thread1 = new Thread(new TwoThreadOneMBDisk(209));
		thread1.setName("First Thread");
		Thread thread2 = new Thread(new TwoThreadOneMBDisk(405));
		thread2.setName("Second Thread");
		
		thread1.start();
		thread2.start();

		thread1.join();
		thread2.join();

		//compute the average values of latency and throughput for two threads
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
			System.out.println("----------For 1 Mbyte data-----------");
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
