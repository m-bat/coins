/* tpassign2.c:  Basic assignment */

int aaaa,b,c;
int x,y;
int maaaain()
{
aaaa=1+  b;
b=aaaa+2;
c=b+aaaa*c; 
x=aaaa+(b+c)*aaaa;
return x;
} 

int main()
{
  maaaain();
  /* SF030620[ */ 
  printf("assign var. in maaaain function \n");
  printf("aaaa=%d b=%d c=%d x=%d \n",aaaa,b,c,x);
  return 0;
  /* SF030620[ */ 
}
