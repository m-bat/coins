/* tpcharfloat1.c:  char to float conversion */

int printf(char*, ...);

int i = 127;
int s = 32767;
float f1, f2;
int main()
{
  f1 = (char)i;
  f2 = (short)s;
  printf("%f %f \n", f1, f2);
  return 0;
} 

