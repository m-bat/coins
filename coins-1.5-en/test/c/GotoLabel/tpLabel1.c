/* tpLabel1.c: Test labeled statement */

int main() {
  int a;
 L1:
 L2:
  a=1;
  
  /* SF030620[ */
  printf("a=%d\n",a);
  return 0;
  /* SF030620] */
}
