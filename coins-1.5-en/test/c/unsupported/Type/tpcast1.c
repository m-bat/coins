/* tpcast1.c:  cast to array (Type) */

void main(){
  char x,y;
  x = ((char*)1000)[0] ; /* cast is disregarded */

  printf("This test should cause 'access violation'. %d\n", x); /* SF030509 */
}
