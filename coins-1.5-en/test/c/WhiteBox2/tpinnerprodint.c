/* tpinnerprodint.c */
/*  Inner production in integer */ 

int aa[10] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
int bb[10] = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 }; 

int printf( char *, ...);
int main()
{
  int i;
  int s;
  s = 0;
  for (i = 0; i<10; i++) {
   s = s + aa[i] * bb[i]; 
  }
  printf(" answer %d \n", s);
  return 0;
}
