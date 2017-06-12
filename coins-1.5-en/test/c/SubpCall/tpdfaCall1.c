/* tpdfaCall1.c -- Data flow test with calls and global/local variables */

int printf(char *, ...);

int g1, g2, g3, ga[10], gb[10][7];

int sum(int pn, int pa[10]) 
{
  int i, j, ls, la[10];

  ls = g1;
  for (i = 0; i < pn; i++)
    ls = ls + pa[i];
  return ls;
}

void sub()
{
}
 
int main()
{
  int li, lj, lx;
  
  g1 = 5;
  sub();
  g2 = 6;
  for (li = 0; li < 10; li++) {
    ga[li] = g1 + li;
    gb[li][0] = g2 + li;
    for (lj = 1; lj < 7; lj++)
      gb[li][lj] = ga[li] + gb[li][lj - 1];
  }
  g3 = sum(10, ga);
  for (li = 0; li < 10; li++) {
    lx = 0;
    for (lj = 1; lj < 3; lj++)
      lx = lx + sum(li, ga); 
    printf(" lx %d \n", lx);
  }
  printf("\n g1 %d g2 %d g3 %d \n", g1, g2, g3);
} 
