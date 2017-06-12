/* tpshift1.c: Shift test */ 

int printf(char*, ...);
int main()
{
  char  c1, c2, c3, c4, c5, c6, c9;
  c1 = -1; 
  c2 = '2' - '0'; 
  c3 = c2 * c2;
  c4 = c3 / c2;
  c5 = c3 % c2;
  c6 = (c3 << 3) + (c3 << c2) + (c3 >> 2) + (c3 >> c2);
  c9 = c3 << 3 + c3 << c2 + c3 >> 2 + c3 >> c2;
  printf("c1 %d c2 %d c3 %d c4 %d c5 %d c6 %d c9 %d\n",
      c1, c2, c3, c4, c5, c6, c9);
  return 0;
} 

