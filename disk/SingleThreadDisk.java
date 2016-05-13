package disk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

public class SingleThreadDisk {
	public void operations() throws IOException {
		// random access write
		RandomAccessFile randomAccessFile = new RandomAccessFile("SampleText.txt",
				"rw");
		randomAccessFile.setLength(1073741824);
		int FILE_SIZE_BYTE = 1;
		int PAGE_SIZE = 4096;
		byte[] BLANK_PAGE = new byte[1024];
		byte[] kbData = new byte[4096];
		byte[] mbData = new byte[1048576];

		long startWrite = System.nanoTime();
		randomAccessFile.write(1);
		double latency = (System.nanoTime() - startWrite) / 1000000.0;
		randomAccessFile.write(2);
		randomAccessFile.write(3);
		randomAccessFile.write(4);
		
		// random access write 1 byte
		startWrite = System.nanoTime();
		for (long i = 0; i < FILE_SIZE_BYTE; i += PAGE_SIZE) {
			randomAccessFile.write(BLANK_PAGE, 0, 1);
		}
		long endWrite = System.nanoTime();
		
		//random access read 1 byte
		randomAccessFile.seek(3);
		int data = randomAccessFile.read(BLANK_PAGE, 0, 1);
		long endRead = System.nanoTime();
		
		double timeTaken = (endRead - endWrite) / 1000000000.0;
		double throughput_read = (1/timeTaken) / 1048576.0;
		
		System.out.println("The latency for 1 Byte block read in ms is " + timeTaken);
		
		timeTaken = (endWrite - startWrite) / 1000000000.0;
		double throughput_write = (1 / timeTaken) / 1048576.0;//total bytes/time taken
		System.out.println("----The values for random file access operations ----");
		System.out.println("The Throughput for reading 1 byte block in MB/s is "
				+ throughput_read);
		System.out.println("The Throughput for writing 1 byte block in MB/s is "
				+ throughput_write);
		System.out.println("The latency for 1 Byte block write in ms is " + latency);
		
		startWrite = System.nanoTime();
		randomAccessFile.write(1);
		latency = (System.nanoTime() - startWrite) / 1000000.0;
		
		// random access write 1 Kbyte
		randomAccessFile.write(2);
		randomAccessFile.write(3);
		randomAccessFile.write(4);
		
		startWrite = System.nanoTime();
		randomAccessFile.write(kbData, 1, 1024);
		endWrite = System.nanoTime();
		timeTaken = (endWrite - startWrite) / 1000000000.0;
		
		throughput_write = (1024 / timeTaken) / 1048576.0;
		
		//random access read 1 KByte
		long startRead = System.nanoTime();
		randomAccessFile.seek((long)Math.random()* 100);
		randomAccessFile.readFully(kbData);
		endRead = System.nanoTime();
		
		timeTaken = (endRead - startRead) / 1000000000.0;
		throughput_read = (1024 / timeTaken) / 1048576.0;
		
		System.out.println("The latency for 1 KB block read in ms is " + timeTaken);
		System.out.println("The Throughput for reading 1KB block in MB/s is "
				+ throughput_read);
		System.out.println("The Throughput for writing 1KB block in MB/s is "
				+ throughput_write);
		System.out.println("The latency for write 1 KB block in ms is " + latency);
		
		// random access write 1 MByte
		startWrite = System.nanoTime();
		randomAccessFile.write(2);
		latency = (System.nanoTime() - startWrite) / 1000000.0;
	
		startWrite = System.nanoTime();	
		randomAccessFile.write(mbData, 0, 1048576);
		endWrite = System.nanoTime();
		
		timeTaken = (endWrite - startWrite) / 1000000000.0;
		throughput_write = (1024 * 1024 / timeTaken) / 1048576.0;
		
		//random access read 1 MByte
		startRead = System.nanoTime();
		randomAccessFile.seek(675);
		randomAccessFile.readFully(mbData);
		endRead = System.nanoTime();
		timeTaken = (endRead - endWrite) / 1000000000.0;
		throughput_read = (1024 * 1024/ timeTaken) / 1048576.0;
		System.out.println("The latency for 1 MB block read in ms is " + timeTaken);
		System.out.println("The Throughput for reading 1MB block in MB/s is "
				+ throughput_read);
		System.out.println("The Throughput for writing 1MB block in MB/s is "
				+ throughput_write);
		System.out.println("The latency for 1 MB block in ms is " + latency);
		// close the random file access pointers
		
		randomAccessFile.close();
		
		System.out
				.println("----The values for sequential access operations----");
		
		// sequential access for 1 Byte file
		String filename = "SampleText1.txt";
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream dos = new DataOutputStream(fos);
		startWrite = System.nanoTime();
		dos.writeInt(42);
		latency = (System.nanoTime() - startWrite) / 1000000.0;
		dos.close();
		fos.close();

		startRead = System.nanoTime();
		FileInputStream fis = new FileInputStream(filename);
		DataInputStream dis = new DataInputStream(fis);
		int value_File = dis.read();
		dis.close();
		fis.close();
		endRead = System.nanoTime();

		timeTaken = (endRead - startRead) / 1000000000.0;
		throughput_write = (1.0/latency)/1048576.0;
		throughput_read = (1.0 / timeTaken) / 1048576.0;
		System.out.println("The Throughput for reading 1B block in MB/s is "
				+ throughput_read);
		System.out.println("The Throughput for writing 1B block in MB/s is "
				+ throughput_write);
		System.out.println("The latency for 1B write block in ms is " + latency);
		System.out.println("The latency for 1B block read in ms is "+timeTaken);

		// sequential access for 1KByte file
		
		fos = new FileOutputStream(filename);
		dos = new DataOutputStream(fos);
		startWrite = System.nanoTime();
		dos.writeInt(62);
		latency = (System.nanoTime() - startWrite) / 1000000.0;
		dos.close();
		fos.close();

		startWrite = System.nanoTime();
		FileOutputStream out = new FileOutputStream(filename);
		PrintStream pout = new PrintStream(out);
		for(int i=0;i<10;i++){
			pout.write(kbData);
		}
		pout.close();
		out.close();
		
		endWrite = System.nanoTime();
		timeTaken = (endWrite - startWrite)/ 1000000000.0;
		throughput_write = (1024.0 * 10.0/timeTaken)/1048576.0;
		
		startRead = System.nanoTime();
		fis = new FileInputStream(filename);
		dis = new DataInputStream(fis);
		dis.readFully(BLANK_PAGE);
		
		dis.close();
		fis.close();
		endRead = System.nanoTime();

		timeTaken = (endRead - startRead) / 1000000000.0;
		throughput_read = (1024.0 /timeTaken) /1048576.0;
		System.out.println("The Throughput for reading 1KB block in MB/s is "
				+ throughput_read);
		System.out.println("The Throughput for writing 1KB block in MB/s is "
				+ throughput_write);
		System.out.println("The latency for write of 1KB block in ms is " + latency);
		System.out.println("The latency for read of 1KB block in ms is "+timeTaken);

		// sequential access for 1MByte file
		
		fos = new FileOutputStream(filename);
		dos = new DataOutputStream(fos);
		startWrite = System.nanoTime();
		dos.writeInt(62);
		latency = (System.nanoTime() - startWrite) / 1000000.0;
		dos.close();
		fos.close();

		startWrite = System.nanoTime();
		out = new FileOutputStream(filename);
		pout = new PrintStream(out);
			pout.write(mbData);
		
		pout.close();
		out.close();
		endWrite = System.nanoTime();
		timeTaken = (endWrite - startWrite)/ 1000000000.0;
		throughput_write = (1048576.0/timeTaken)/1048576.0;

		startRead = System.nanoTime();
		fis = new FileInputStream(filename);
		dis = new DataInputStream(fis);
		dis.readFully(mbData);
		
		dis.close();
		fis.close();
		endRead = System.nanoTime();
		timeTaken = (endRead - startRead) / 1000000000.0;
		throughput_read = (1048576.0 /timeTaken) /1048576.0;
		System.out.println("The Throughput for reading 1MB block in MB/s is "
				+ throughput_read);
		System.out.println("The Throughput for writing 1MB block in MB/s is "
				+ throughput_write);
		System.out.println("The latency for 1MB block read in ms is " + latency);
		System.out.println("The latency for 1 MB block write in ms is "+timeTaken);
		
	}

	public static void main(String[] args) throws IOException {
		SingleThreadDisk disk_perf = new SingleThreadDisk();
		disk_perf.operations(); //all the random and sequential operations are implemented in this function

	}
}
