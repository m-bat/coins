/* tpcse1.c  Common subexpression elimination test */
/* Produced while compiling crc.c by hirOpt=loopif/loopexp/cse */

int printf(char*, ...);

unsigned char aa[] = "asdffeagewaHAFEFaeDsFEawFdsFaefaeerdjgpim23";
unsigned short icrc(unsigned short crc, unsigned char *lin, unsigned int len,
                    short jinit, int jrev)
{
  static unsigned short icrctb[256];
  unsigned short tmp1, j, cword = crc;
  j = 8;
  { tmp1 = (unsigned short )(((int )((*((unsigned char * )((((char*)lin))
    + (((int )1) * ((int )(j)))))))) ^ 
    ((int )((unsigned char )(((int )(cword)) >> ((unsigned char )8))))); }
  cword = (unsigned short )(((int )(icrctb[(int )(tmp1)])) ^ 
    (((int )((unsigned char )(((int )(cword)) & ((int )255)))) 
    << ((unsigned char )8)));
  { tmp1 = (unsigned short )(((int )((*((unsigned char * )((((char*)lin))
    + (((int )1) * ((int )((((int )(j)) + ((int )1)))))))))) ^ 
   ((int )((unsigned char )(((int )(cword)) >> ((unsigned char )8))))); }
  { tmp1 =  tmp1 + (unsigned short )(((int )((*((unsigned char * )((((char*)lin))
    + (((int )1) * ((int )((((int )(j)) + ((int )1)))))))))) ^ 
   ((int )((unsigned char )(((int )(cword)) >> ((unsigned char )8))))); }
  return tmp1;
}
int main()
{
  unsigned short i1;
  i1 = icrc(4, aa, 40, (short) 0, 1);
  printf("%u\n", i1);
  return 0;

}

