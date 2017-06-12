/* smpTest1.c: SMP test 1 by Yuba-Honda */

#include <stdio.h>

/** #define MAX 100
**/

int main(void) {
  int i, i1, i2, i3, i4;
  int a1[100], b1[100], a2[100], b2[100], a3[100],
      b3[100], b4[100], b5[100];

  for(i=0; i<100; i=i+1){  /* Task 0 */
    b1[i]=i;
    b2[i]=i+1;
    b3[i]=i+2;
    b4[i]=i+3;
  }
  /* SF030620 */
  printf("b1=%d b2=%d b3=%d b4=%d\n",b1[99],b2[99],b3[99],b4[99]);

  for(i1=0;i1<100;i1=i1+1){  /* Task 1 */
    a1[i1]=b1[i1]+i1+4;
  }
  /* SF030620 */
  printf("a1=%d ",a1[99]);

  for(i2=0;i2<100;i2=i2+1){  /* Task2 */
    a2[i2]=b2[i2]+i2+4;
  }
  /* SF030620 */
  printf("a2=%d ",a2[99]);

  if(a2[100-1]%2==1){
    for(i3=0;i3<100;i3=i3+1){ /* Task 3 */
      a3[i3]=b3[i3]+i3+4;
    }
  }
  else{
    for(i4=0;i4<100;i4=i4+1){ /* Task 4 */
      a3[i4]=b4[i4]+i4+4;
    }
  }
  /* SF030620 */
  printf("a3=%d\n",a3[99]);


  for(i=0;i<100;i=i+1){     /* Task 5 */
    b1[i]=a1[i];
    b2[i]=a2[i];
    b5[i]=a3[i];
  }
  /* SF030620 */
  printf("b1=%d b2=%d b5=%d\n",b1[99],b2[99],b5[99]);

  return(0);
}


