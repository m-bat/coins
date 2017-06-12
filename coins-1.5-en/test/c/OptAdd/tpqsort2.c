/* tpqsort2.c  Quick sort program with swap */

#define N 10000000
  int aa[N];
int printf(char*, ...);

void qsort(int v[], int left, int right)
{
 int i,last;
 void swap(int v[], int i, int j);
 
 if (left >= right)
   return; 
 swap(v, left, (left+right)/2);
 last=left;
 for (i = left+1; i <= right; i++)
  if (v[i] < v[left])
    swap(v, ++last, i);
 swap(v, left, last);
 qsort(v, left, last-1);
 qsort(v, last+1, right);
}

void swap(int v[], int i, int j)
{
 int temp;
 temp = v[i];
 v[i] = v[j];
 v[j] = temp;
}

int main()
{
  int i, j;
  for (i = 0; i < N; i++) {
    aa[i] = N-i;
  }
  qsort(aa, 0, N-1);
  printf("N=%d \n %d %d %d %d %d %d %d %d \n", N,
         aa[0], aa[1], aa[2], aa[3], aa[N-4], aa[N-3], aa[N-2], aa[N-1]);
  return 0;
}

