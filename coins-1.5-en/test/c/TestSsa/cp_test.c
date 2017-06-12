#include<stdio.h>
int main () 
{
  int i;
  int j;
  int k;
  int l;

  i = 6;
  j = 1;
  k = 1;

  while (i != j) {
    if (i == 6)
      k = 0;
    else
      i = i + 1;

    i = i + k;
    j = j + 1;
  }
	
  printf("%d %d %d\n",i,j,k);
}
