#include<stdio.h>
#include<time.h>
#include<stdlib.h>
#include<string.h>
#define MEMORY_CHUNK_SIZE 15728640
#define BUFFER_BYTE 1
#define BUFFER_KBYTE 1024
#define BUFFER_MBYTE 1048576
void main(){
	char *array1,*array2;
	struct timeval start,end;
	double diff;
	time_t t;
	

	srand((unsigned) time(&t));//initialise the rand function	
	array1 = (char *)malloc(MEMORY_CHUNK_SIZE); //create array of large size
	array2 = (char *)malloc(MEMORY_CHUNK_SIZE);
	int index = 0;

	//sequential read + write
	gettimeofday(&start,NULL);	
	while(index <= 1048576){
		memcpy(&array2[index],&array1[index],BUFFER_BYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	printf("The latency in ms for 1 byte sequential is %lf\n",diff/1048576.0);
	double throughput_byte = (1048576.0 * 1000000 / diff)/(1024.0 * 1024);
	printf("The throughput in MB/s for 1 byte sequential is %lf\n",throughput_byte);
	
	index = 0;
	gettimeofday(&start,NULL);
	while(index < 5){
		memcpy(&array2[index],&array1[index],BUFFER_KBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	printf("The latency for 1KB sequential in ms is %lf\n",diff*1000);
	double throughput_kbyte = (5.0 * 1000000 / diff)/(1024.0);
	printf("The throughput in MB/s for 1KB sequential is %lf\n",throughput_kbyte);
	
	index=0;
	while(index < 2){
		memcpy(&array2[index],&array1[index],BUFFER_MBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	printf("The latency for 1 MB sequential in ms is %lf\n",diff*1000);
	double throughput_mbyte = (2 * 1000000 / diff);
	printf("The throughput in MB/s for 1 MB sequential is %lf\n",throughput_mbyte);
	
	//random read+write
	int offset1 = rand() % 120; //calculate random offsets
	int offset2 = rand() % 50;
 	
	index = 0;
	gettimeofday(&start,NULL);	
	while(index <= MEMORY_CHUNK_SIZE){
		memcpy(&array2[index+offset2],&array1[index+offset1],BUFFER_BYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	printf("The latency in ms for 1 byte random is %lf\n",diff * 1000);
	throughput_byte = (15728640.0 * 1000000 / diff)/(1024.0 * 1024);
	printf("The throughput in MB/s for 1 byte random is %lf\n",throughput_byte);
	
	index = 0;
	offset1 = rand() % 140;
	offset2 = rand() % 60;
	gettimeofday(&start,NULL);
	while(index < 7){
		memcpy(&array2[index+offset1],&array1[index+offset2],BUFFER_KBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	printf("The latency for 1KB random in ms is %lf\n",diff * 1000);
	throughput_kbyte = (7.0 * 1000000 / diff)/(1024.0);
	printf("The throughput in MB/s for 1KB random is %lf\n",throughput_kbyte);
	
	index=0;
	offset1 = rand() % 199;
	offset2 = rand() % 88;
	while(index < 9){
		memcpy(&array2[index+offset2],&array1[index+offset1],BUFFER_MBYTE);
		index++;
	}
	gettimeofday(&end,NULL);
	diff = end.tv_usec - start.tv_usec;
	printf("The latency for 1 MB random in ms is %lf\n",diff * 1000);
	throughput_mbyte = (9.0 * 1000000 / diff);//total number of bytes divided by time elapsed
	printf("The throughput in MB/s for 1 MB random is %lf\n",throughput_mbyte);
}
