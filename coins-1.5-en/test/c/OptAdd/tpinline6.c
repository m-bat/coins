/* tpinline.6 Inline expansion (absolute difference) */
int printf(char*, ...);
int absdiff( int p1, int p2 );
int main()
{
  int a=1, b=3, c;
  c = absdiff(a, b);
  printf("c=%d\n", c);
  return 0;
} 

int absdiff( int p1, int p2 )
{
  if (p1 > p2)
    return p1 - p2;
  else
    return p2 - p1;
}
  
