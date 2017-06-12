/* tpshiftError.c
*/
void h() {
  int i;
  i <<= i;
}

/* SF030620[ */
main()
{ h(); printf("tpshiftError.c\n"); }
/* SF030620] */
