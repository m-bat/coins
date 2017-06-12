/* basicStmt.c  Control flow and label test. */

int puts(char*);

stmt()
{
  int a;

  // (1) unreachable statement
  goto LABEL_1;
  puts("(1) 1");
  puts("(1) 2");
  LABEL_1:;

  // (2) jump distance is zero
  goto LABEL_2;
  LABEL_2:;

  // (3) not referenced label
  LABEL_3:;

  // (4) block without statement
  { int a; }

  // (5) leave only the part with the possibility for the side effect to occur.
  a + puts("(5) 1") + 1 + puts("(5) 2");

  // (1+2+3)
  goto LABEL_11;
  puts("(1+2+3) 1");
  goto LABEL_12;
  puts("(1+2+3) 2");
  LABEL_13:
  puts("(1+2+3) 3");
  LABEL_11:;
  puts("(1+2+3) 4");
  LABEL_12:;

  // (3+4+5)
  {
    int i, *p, a[3];
    i = 1;
    a[i+0];
    LABEL_21:
    p+i+1;
    LABEL_22:
    &a[2];
  }
}

int main()
{
  stmt();
  return 0;
}
