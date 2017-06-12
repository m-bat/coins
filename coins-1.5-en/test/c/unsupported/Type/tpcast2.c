/* tpcast2.c:  cast to struct (Type) */
/*          cast information is lost */
  struct port { unsigned int address; unsigned int data;};
void main(){
  long x;
  unsigned int y;
  x = *(unsigned int *)1000;
  y = ((struct port volatile *)100)->data ; /* cast is disregarded */

  printf("This test should cause 'access violation'.%d %d\n", x, y); /* SF030509 */
}
