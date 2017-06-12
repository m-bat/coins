/* filePutc1.c */
/* Write BUFSIZE bytes to stdout file by putc */
/* Usage:
    ./a.exe > outfile
*/ 
#include <stdio.h>

/*
#define BUFSIZE 8192
#define COUNT 10000
*/
#define BUFSIZE 16
#define COUNT   10

char buffer[BUFSIZE];

int main( int argc, char *argv[])
{
  int i, j, nbyte;
  for (i = 0; i < BUFSIZE; i++)
    buffer[i] = '0'+i%10;
  printf("write %d bytes %d times to stout by putc( ) \n", BUFSIZE, COUNT);
  for (i = 0; i < COUNT; i++) {
    putchar(' ');
    for (j = 0; j < BUFSIZE; j++)
      putchar(buffer[j]);
  }
}
