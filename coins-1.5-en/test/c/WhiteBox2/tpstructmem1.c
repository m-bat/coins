/* tpstructmem1.c:  Structure memory layout  */
/*    Fukuda-s mail 040210 */

int printf(char*, ...);

  main()
  {
    struct S{int mem;} s1, s2;
    s1.mem = 1;
    s2.mem = 2;
    printf("%d\n",s1.mem);
    printf("%d\n",s2.mem);
    printf("%d %d\n",s1.mem,s2.mem);
  }
