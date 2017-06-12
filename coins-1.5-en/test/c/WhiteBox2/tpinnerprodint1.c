/* tpinnerprodint1.c */
/*  Inner production in integer 
    Initial values are set by while loop. */ 

/*
int aa[10] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
int bb[10] = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 }; 
*/

int printf(char*, ...);
int aa[10], bb[10];
int i, s;
int main()
{
  i = 0;
  while (i < 10) {
    aa[i] = i;
    bb[i] = 10 - i;
    i++;
  }
  s = 0;
  i = 0;
  while (i < 10) {
   s = s + aa[i] * bb[i]; 
   i++;
  }
  printf(" %d\n", s);
  return 0;
}
