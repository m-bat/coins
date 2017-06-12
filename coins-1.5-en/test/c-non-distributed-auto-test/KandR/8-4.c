/* KandR/8-4.c 
   Give KandR/8-234.in as input file.
*/

#include "syscalls.h"

int get(int fd, long pos, char *buf, int n)
{
	if (lseek(fd, pos, 0) >= 0)
		return read(fd, buf, n);
	else
		return -1;
}

main()
{
	char buf[16] = "   \n";
	
	if (get(0, 4, buf, 3) > 0)
		write(1, buf, 4);
}
