/* tpassign2-1.c:  void main */

int aaaa,b,c;
int x,y;

/* void main() /* SF030620 */
main() /* SF030620 */
{
  aaaa=1+  b;
  b=aaaa+2;
  c=b+aaaa*c;
  x=aaaa+(b+c)*aaaa;
  printf("assign in void main \n"); /* SF030620 */
  printf("aaaa=%d b=%d c=%d x=%d \n",aaaa,b,c,x); /* SF030620 */
}

