/* tpstructinit6.c : Structure bit field initiation  (Jis C p. 73)*/ 

int printf(char*, ...);

  struct tag {
    unsigned t:4;
    const     :5;
    int      r:5;
  };

int 
main()
{
  struct tag str1 = { 15, -15, 12 };  
  printf("str1 %d %d \n", str1.t, str1.r);
}

