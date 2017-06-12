/* tpfuncptr9.c  Call by function pointer */
/*   Fukuda mail 040302 */

int printf(char*, ...);

void 
f()
{ 
  printf("f was called \n");
}
main()
{ 
  void (*g)() = f;
  g(); 
}

