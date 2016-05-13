#compiler to be used is gcc
CC=gcc
#define the target file
all:output
output: MemoryTwoThread.o
	$(CC) MemoryTwoThread.o -pthread -o output
	$(CC) -c MemoryTwoThread.c
clean :
	rm MemoryTwoThread.o