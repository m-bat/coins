/* tpconstindex1.c: constant index */ 

int printf(char*, ...);

int main()
{
  int i, n;
  char c;
  char ca[10];
  ca[1] = '1';
  ca[2] = 'a';
  
  c = ca[1] + ca[2];  
  printf(" %d", c);
}


