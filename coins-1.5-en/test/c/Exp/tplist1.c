/* tplist1.c:  List */
int    i1, i2, i3;
int main()
{
  i1 = 2147483647;
  /*i2 = -2148483648; /* SF030620 */
  i2 = -2147483648; /* SF030620 */
  (i1, i2);
  i3 = (i2, i1);
  printf("i3=%d\n",i3); /* SF030620 */
  return 0;
}

