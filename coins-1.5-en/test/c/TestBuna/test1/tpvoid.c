/* tpvoid.c:  void function (simple) */

void f(){ /* void main(){ SF030514 */
  int x,y;
  x = 0;
  y = 0;
  x = y;
}
main(){ /* SF030514 */
  f(); /* SF030514 */
  printf("void function test\n"); /* SF030514 */
}
