/* tpstructinit5.c : Structure bit field initiation in declaration */ 

int printf(char*, ...);

  struct tag {
    unsigned s:4;
    const    t:5;
    int      r:5;
  };

int 
main()
{
  struct tag str1 = { 15, -15, 12 };  
  printf("str1 %d %d %d \n", str1.s, str1.t, str1.r);
}

