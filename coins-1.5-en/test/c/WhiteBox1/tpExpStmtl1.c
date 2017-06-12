/* tpExpStmt1.c   Expression as a statement (value is not used). */

int x;

int sub(int p)
{
  x = p;
  return x+1;
}

int main()
{
  int a;
  a = 7;
  a+sub(1);
  printf("%d \n", x);
  return 0;
}
