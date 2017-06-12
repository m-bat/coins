/* tpnomain.c:  Basic assignment without main */
int a, b, c;
int x, y, z;
int func()
{
  a = 1;
  b = a + 2;
  c = b + a * c;
  x = a + (b + c) * a;
  return x;
}

/* SF030620[ */
main()
{
  printf("basic assignment test.\n");
}
/* SF030620[ */
