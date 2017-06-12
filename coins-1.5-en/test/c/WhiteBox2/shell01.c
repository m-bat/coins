/* shell01.c -- Kernel part of shell sort */

#include <stdio.h>

void shellsort(int v[], int n)
{
  int gap, i, j, temp;
  for (gap = n/2; gap > 0; gap /= 2)
    for (i = gap; i < n; i++)
      for (j=i-gap; j>=0 && v[j]>v[j+gap]; j-=gap) {
        temp = v[j];
        v[j] = v[j+gap];
        v[j+gap] = temp;
      }
}

#define SIZE 20  /**/

main()
{
  int data[SIZE];
  int i;
  /* srand(500); */
  srand(11);
  for(i=0; i<SIZE; i++){
    data[i] = rand();
  }
  shellsort(data, SIZE);
  for (i = 0; i < SIZE; i++)
    printf("%d\n",data[i]);
}
