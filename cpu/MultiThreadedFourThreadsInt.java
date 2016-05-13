package cpu;

public class MultiThreadedFourThreadsInt implements Runnable {
	int a = 1,b = 0; 
	public void run() {

		for (int i = 0; i < 6998000; i++) {
			a++;
			a--;
			b = a * a;
			b = a / 2;
			a = a * a +  b * b + 2 * a * b;
 			a++;
                        a--;
                        b = a * a;
                        b = a / 2;
                        a = a * a +  b * b + 2 * a * b;
			a++;
			a = a + i;
		}

	}

	public static void main(String[] args) {
		MultiThreadedFourThreadsInt nr = new MultiThreadedFourThreadsInt();
		Thread t1 = new Thread(nr);
		t1.setName("T1");
		Thread t2 = new Thread(nr);
		t2.setName("T2");
		Thread t3 = new Thread(nr);
		t3.setName("T3");
		Thread t4 = new Thread(nr);
		t4.setName("T4");

		long start = System.nanoTime();
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		double diff = System.nanoTime() - start;
		
		double iops = Math.ceil((23 * 6998000)/diff);
		System.out.println("The value of time elapsed for four threads int is "+(diff * 0.000000001)+" seconds");
	    	System.out.println("The value of IOPS "+iops);		//watch -n 1 java MultiThreadedFourThreadsInt
	}
}
