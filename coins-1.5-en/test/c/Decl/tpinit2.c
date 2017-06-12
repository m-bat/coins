/* tpinit2.c  Initial value for array (Decl) */
int main()
{
  char c1[2][2] = { 1, 2, 3, 4};
  char c2[][2]  = { 5, 6, 7, 8};
  char c3[][2]  = { 9, 10, {11, 12}};
  printf("%d %d %d \n", c1[1][1], c2[0][1], c3[1][0]);
  return 0;
}

