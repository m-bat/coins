/* tpScopeNakata1 020527 error report */

int f()
{ 
  int f;
  f = 1; 
  goto f;
  f = 2;
  f: ;
  return f; 
}

int main()
{
  printf("%d \n", f());
}

