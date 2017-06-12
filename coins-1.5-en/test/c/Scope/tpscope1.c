/* tpscope1.c :  Scope test */

int varx, vary, varz[10];
struct address {
  char* name;
  int   zip;
};
struct addressx {
  char* name;
  int   zip;
};
struct address  tanaka;
struct addressx yamada;

int main()
{
  int i, j, a[10];
  struct { int x; int y; } s;

  tanaka.name = "Tanaka";
  tanaka.zip = 184;
  if (tanaka.zip == 0)
    tanaka.zip = 001;
  yamada.name = "Yamada";
  yamada.zip = 184;
  s.x = 1;
  varx = 1;
  vary = varx + 1;
  varz[0] = vary;
  varz[1] = varz[0] + 1;
  a[0] = varz[0];
  a[1] = a[0] + varz[1];
  i = vary - 1;
  j = i - 1;
  s.x = i;
  s.y = s.x + j;
  /* SF030609[ */
  printf("Tanaka = [%s,%d]\n",tanaka.name,tanaka.zip);
  printf("Yamada = [%s,%d]\n",yamada.name,yamada.zip);
  printf("s = [%d,%d]\n",s.x,s.y);
  printf("varx,vary,varz = %d,%d,[%d,%d,...]\n",varx,vary,varz[0],varz[1]);
  printf("i,j,a = %d,%d,[%d,%d,...]\n",i,j,a[0],a[1]);
  /* SF030609] */
  return 0;
}

