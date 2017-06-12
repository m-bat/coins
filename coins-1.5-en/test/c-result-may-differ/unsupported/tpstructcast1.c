/* tpstructcast1.c  cast by structure */ /* Moved from WhiteBox1 */
/*    Address expression to be evaluated at compile time */
/*    Fukuda mail 021126 */

  int a, b;  /*(1)*/
  int *p[] = {&a+1,&b+2};

  int buf1[16];  /*(2)*/
  int buf2[ &buf1[16]-&buf1[0] ];  /* bad integer constant ? */

  struct ST{int a;char b;} st;  /*(3)*/
  int displace[2] = { (int)&((struct ST*)0)->a, (int)&((struct ST*)0)->b };

  main()
  {
    printf("sizeof(buf1)=%d\nsizeof(buf2)=%d\n",sizeof(buf1),sizeof(buf2));
    printf("ST...a=%d\nST...b=%d\n",displace[0],displace[1]);
  }
