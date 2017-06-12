/* tpbitfield1.c Bit field test */

int printf(char*, ...);

struct {
  char c1; 
  unsigned int is_keyword : 1;
  unsigned int is_extern  : 1;
  unsigned int is_static  : 1;
  /*  unsigned int :0; */
  char c2;
  int i1;
  int ib1:2;
  int ib2:20;
  int ib3:5;
  int i2;
  unsigned int cb1:3;
  unsigned int cb2:3;
} bits;

int main()
{
  bits.is_extern = bits.is_static = 1;
  bits.is_keyword = 0;
  bits.c2 = 'a';
  bits.i1 = 11;
  bits.ib1 = 3;
  bits.ib2 = 15;
  bits.ib3 = 7;
  bits.i2  = 333;
  bits.cb1 = 5;
  bits.cb2 = 6;
  printf("%d %d \n", bits.is_extern, bits.is_static);
  printf("%d %d %d %d %d %d %d %d \n", bits.c2, bits.i1, bits.ib1, 
         bits.ib2, bits.ib3, bits.i2, bits.cb1, bits.cb2);
  return 0;
}
