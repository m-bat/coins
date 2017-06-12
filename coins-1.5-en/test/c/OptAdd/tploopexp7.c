/* tploopexp7.c Loop expansion and type conversion test */

int printf(char*, ...);
int n = 200;
unsigned char pos[300];
int main()
{
  unsigned char tos[300] = {1};
  unsigned char v, s, t, j;
  for (v = 0; v < n; v++) {
    pos[v] = v;
    tos[v] = v + 1;
  }
  s = 0;
  t = 0;
  for (v = 1, j = 1; v < n; v++, j++) {
    s = s + j;
    t = t + 1;
  }
  printf("%d %d %d %d %d\n", pos[199], tos[198], v, s, t);
  return 0; 
}
