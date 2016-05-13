package cpu;

public class SingleThread {
	public static void main(String[] args) {
		float a = 0.66f,c = 0.0f;
		int b =0,d =10,e=9,g =133;
		long start = System.nanoTime();
		//calculate the number of IOPS
		for(int i=0;i<9220000;i++){
			e = (b++) + (d--) +(e + (b * b) + b/10) + (b * b + 2 * g) + (b++) + (d++) + b + d;
			b++;
			d--;
			g += e + (b * b) + b/10 ;
			b =  (b * b + 2 * g) + (b++);
			d++;
			e = b + d;
		}
		double elapsed = (System.nanoTime() - start);
		System.out.println("The time elapsed for IOPS is "+(elapsed * 0.000000001)+" seconds");
		double iops = Math.ceil((33*9220000)/elapsed); //2147483647l -> is value of Integer.MAX_VALUE
		System.out.println("The GIOPS is "+iops);
		
		start = System.nanoTime();
		//calculate the number of FLOPS
		 for(int i=0;i<1550000;i++){
			 a = a + 7.5f;
			 a = a - 1.5f;
			 c = a * a;
			 c = a / 1.5f;
			 c = a + 2.777f;
			 c = a + 1.3f;
			 a = c + 1.5f;
			 c = c + 1.8f;
			 a += 1.6f;
			 c += 1.8f;
			 c = a + 7.5f;
			 a = a - 1.5f + (a * a)+(a + c) +(c++) +(a+c) +(a * 2.777f) + (c/a)+ (a + c) + (a+ 1.5f) + (a - 1.5f) + (a * a) + (a/1.5f) + (a + c) + (c++) + (a + c) + (a * 2.777f) + (c/a) + (a + c) + (a+ 1.5f);
			 a = a + 7.5f;
			 a = a - 1.5f;
			 c = a * a;
			 c = a / 1.5f;
			 c = a + 1.3f;
         }

		elapsed = (System.nanoTime() - start);
		System.out.println("The time elapsed for FLOPS is "+(elapsed*0.000000001) +" seconds");
		double flops = Math.ceil(((53*1550000)/elapsed));
		System.out.println("The GFLOPS is "+flops);
	}
}
