/* matMult3.c Nakata mail 060219 */

#include <stdio.h>
#include<stdlib.h>

#define N 500
#define L 500
#define M 500

typedef float matNL[N][L];
typedef float matLM[L][M];
typedef float matNM[N][M];
void multiply(matNM c, matNL a, matLM b)
{
        int i, j, k;
        float s;

        for (i = 0; i < N; i++)
                for (j = 0; j < M; j++) {
                        s = 0;
                        for (k = 0; k < L; k++) s += a[i][k] * b[k][j];
                        c[i][j] = s;
                }
}

int main()
{
        int i, j;
        static matNL a;
        static matLM b;
        static matNM c;

        for (i = 0; i < N; i++)
                for (j = 0; j < L; j++)
                        a[i][j] = rand() / (RAND_MAX / 10 + 1);
        for (i = 0; i < L; i++)
                for (j = 0; j < M; j++)
                        b[i][j] = rand() / (RAND_MAX / 10 + 1);
        multiply(c, a, b);

   /*=== Eraze print if execution time is to be measured. ===*/
   for (i = 0; i < 10; i++) {
      printf("\ni=%d ", i);
    for (j = 0; j < 10; j++) {
      printf(" %7.2f", c[i][j]);
    }
  } 
  return 0;
}

