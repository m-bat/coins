/* tpblockScope5.c:  redeclared variable message is wrong Mori mail 030903 */

#define EXTENDED_BUFFER() \
  do {  \
    unsigned char *old_buffer = tmp; \
  }while(0)
  
int foo()
{
  unsigned char *tmp;
  EXTENDED_BUFFER();
  EXTENDED_BUFFER();
}

#define EXTENDED_BUFFER2() \
  do {  \
    unsigned char *old_buffer = tmp; \
    tmp++;  \
  }while(0)
  
int foo2()
{
  unsigned char *tmp;
  EXTENDED_BUFFER2();
  EXTENDED_BUFFER2();
}

