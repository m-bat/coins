/* tpStringArray1.c:  String array 1 */

int printf(char*, ...);

int aa[][2] = {{1,2}, {3}, {4,5}};
char *cc[][3] = {{"ab", "cde", "efgh"}, {"ii", "jj"}};
int main()
{
  cc[1][2] = "kk";
  printf(" %d %d %d %d\n", aa[0][0], aa[0][1], aa[1][0], aa[1][1]);
  printf(" %s %s %s %s %s\n", cc[0][0], cc[0][1], cc[1][0], cc[1][1], cc[1][2]);
  return 0;
}

