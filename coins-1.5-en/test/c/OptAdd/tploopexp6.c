/* tploopexp6.c Loop expansion and type conversion test */

int printf(char*, ...);
int n = 300;
unsigned char pos[300];
int main()
{
  unsigned char tos[300] = {1};
  int j;
  unsigned char v;
  for (v = 0; v < 100; v++) pos[v] = v;
  printf("%d %d \n", pos[90], v);
  for (v = 0, j = 0; (v < n)&&(j < n); v++, j++) {
    pos[v] = v;
    tos[v] = v + 1;
  }
  printf("%d %d %d\n", pos[257], tos[258], v);
  return 0; 
}
