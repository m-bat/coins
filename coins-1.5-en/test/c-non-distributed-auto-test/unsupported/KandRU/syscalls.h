#ifndef __KANDR_SYSCALLS_H
#define __KANDR_SYSCALLS_H

#ifndef BUFSIZ
#define BUFSIZ 512
#endif

#ifndef EOF
#define EOF -1
#endif

int read(int fd, char *buf, int n);
int write(int fd, char *buf, int n);
long lseek(int fd, long offset, int origin);
void *malloc(unsigned int);

#endif
