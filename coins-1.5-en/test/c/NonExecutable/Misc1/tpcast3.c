/* tpcast3.c
*/
void g() {
  int i;
  i = i + (unsigned char)i;
}


/* SF030620[ */
main()
{ g(); printf("cast test 3\n"); }
/* SF030620] */
