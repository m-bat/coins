/* sort_shell_1.c */

#include <stdio.h>
#include <math.h>

#define N 100     /* Number of data */ 

int main(void)
{
    int a[N];
    int i = 0, j = 0, t = 0;

    for(i=0;i<N;i++)       /* Generate N random numbers */
        a[i]=rand();

    for (i=1; i<N; i++){
        for (j=i-1; j>=0; j--){
            if (a[j] > a[j+1]){
                t=a[j];
                a[j]=a[j+1];
                a[j+1]=t;
            }
            else
                break;
        }
    }

    for (i=0;i<N;i++)
        printf("%8d ",a[i]);
    printf("\n");

}
