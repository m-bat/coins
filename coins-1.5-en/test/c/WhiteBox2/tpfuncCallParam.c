/* tpfuncCallParam.c  Function call as a parameter  */
/*   Fukuda mail 040302 */

int printf(char*, ...);

char 
f(char a)
{ 
  printf("a=%d ",a);
  return a; 
}

void 
g(int b, char c)
{ 
  printf("b=%d c=%d\n",b,c); 
}

int main()
{ 
  g(1,f(2)); 
  return 0;
}




