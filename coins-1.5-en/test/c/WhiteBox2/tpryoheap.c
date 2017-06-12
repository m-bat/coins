/* tpryoheap.c heap sort written by ryo */

#include <stdio.h>
#include <limits.h>
/*
#define LIMIT 100

typedef int ElementType;
typedef struct node Node;
*/

int a[100+1];
int cmp_count;

struct node {
  int element;
  struct node *left,*right;
};
void swap(int *x,int *y)
{
  int tmp;

  tmp=*x;
  *x=*y;
  *y=tmp;
}

void downheap(int n,int i)
{
  int j;
  int tmp;

  tmp=a[i];
  while((j=i*2+1)<=n){
    if(++cmp_count && j<n && a[j]<a[j+1]) j++;
    if(++cmp_count && tmp>=a[j]) break;
    a[i]=a[j];
    i=j;
  }
  a[i]=tmp;
}
void generate(void)
{
  int i,x=11073;

  for(i=0;i<100;i++){
    x=x*1103+125;
    a[i]=x/656+368;
  }
  a[100]=INT_MAX;
}
void heapsort(int n)
{
  int i,m;

  for(i=(n-2)/2;i>=0;i--) downheap(n-1,i);
  m=n-1;
  while(m>0){
    swap(&a[0],&a[m]);
    downheap(--m,0);
  }
}
main()
{
  generate();
  heapsort(100);
  printf(" compare-count %d\n",cmp_count);
}

