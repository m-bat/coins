/* mbdisp1.c: mb-gcc test 2 */ 
/*   Displacement */

int printf(char*, ...);
  int ga[100000];
int main()
{
  int aa[10000];
  int a, b, c;
  int i, j, k1, k2, k3;
  k1 = 1;
  k2 = 9999;
  k3 = 99999;
  
  for (i = 0; i < 100000; i++) {
    ga[i] = i;
  }
  for (i = 0; i < 10000; i++) {
    aa[i] = i;
  }
  a = ga[k1 + 1023] + ga[k1 + 32767];
  b = aa[k2 - 1023] + aa[k2 - 4095];
  c = ga[k1 + 65536];
   
  printf("a=%d b=%d c=%d \n", a, b, c);
  return 0;
} 

