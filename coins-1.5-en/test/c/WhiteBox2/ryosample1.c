/* Tanaka Ryo test program 040128 */

#include <stdio.h>

#define N 10000
#define M 10000
void f(int a[], int b[]){
  int i;
  for(i = 0; i < M; i++)
    b[i] = a[i];
}

int main(void){
  int i, n, a[M], b[M];

  for(i = 0; i < M; i++){
    a[i] = i;
  }
  for(n = 0; n < N; n++){
    f(a, b);
  }
  printf(" %d %d %d %d \n", b[0], b[1], b[M-2], b[M-1]);
}
