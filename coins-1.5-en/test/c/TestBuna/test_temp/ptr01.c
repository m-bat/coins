int printf(char *s, ...);
int  a[10],b,c,i,*p,*q,*r;

int main() 
{ 
  p=a; 
  q=&b; 
  c=*q+1;
  printf("c = %d\n", c);
  return 0;
} 