int printf(char *s, ...);

main()
{
  int a, b, c, i;
 
  a = 1;
  b = 2;
  if (a == 1) {
    if (b == 2) {  
      for (i = 1; i < 10; i++) {
        c = a + b;
      }
    }else {
      c = a + b;
    }
  }else {
    a = 3;
    c = a + b;  
  }
  c = a + b;
  printf("c = %d\n", c);
}
