/* tplonglong2.c  long long  (Decl) */
int main()
{
  long long ll, ll2;
  ll = 1;
  ll2 = ll<<1;    /* (<< ll (<cast i->j> 1))  (cast is unnecessary) */
  printf("%lld \n", ll2);
  return 0;
}

