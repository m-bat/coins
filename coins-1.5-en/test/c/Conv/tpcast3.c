/* tpcast3.c
*/
void g() {
  int i;
  i = i + (unsigned char)i;
}
/* SF030620[ */
int main()
{
  g();
  printf("cast test\n");
  return 0;
}
/* SF030620] */
