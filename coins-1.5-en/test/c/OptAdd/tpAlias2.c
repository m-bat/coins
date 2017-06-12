/* tpAlias2.c  Alias test */

int gx, gy, ga[10], gb[10];
int *gp1, *gp2;

struct nameList {
  char *name;
  int  number;
} student;

struct point {
  float x1;
  float y1;
} gpoint;

int *func1( int pa[10], int pb[10], int *pi)
{
  int i;
  for (i = 1; i < 10; i++)
    pa[i] = pb[i] + *pi;
  return &pa[0];
}

float func2( struct point ppoint )
{
  return ppoint.x1 * ppoint.y1;
}

int main()
{
  int i, j, k, m, n;
  int x, y, xx, yy, zz;
  int *px, *py, *pz;
  int a[30], b[10];
  struct point lPoint = { 2.0, 3.0 };
  struct nameList person = {"tanaka", 99};
  float area;
  float fa1[10], fa2[20];
  float *pf1, *pf2, *pf3;
  j = 0;
  n = 30;
  m = 20;
  xx = 3;
  a[0] = 1;
  a[1] = 1;
  for (i = 2; i < n; i++) {
    a[i] = a[i-1] + a[i-2];
  }
  b[j] = a[j];
  b[j+1] = a[j] + a[j+1];
  zz = 5;
  px = &xx;
  *px = 1;
  px = &yy;
  py = &yy;
  *px = 2;
  x = *px;
  y = *px + *px + *py;
  pf1 = fa1;
  pf2 = fa2;
  *pf1 = 1.0;
  pf3 = pf1 + 1;
  *pf3 = 2.0;
  fa2[j] = *pf3;
  fa2[1] = *pf1 + fa1[0];
  fa1[j] = *pf1 + *pf2 + *pf3;
  px = func1(ga, gb, px);
  printf("%d %d %d %d %d %f %f\n", x, y, xx, yy, *px, *pf1, fa2[1]);
  gpoint.x1 = 3.0;
  gpoint.y1 = 4.0;
  area = func2(gpoint);
  student.name = "suzuki";
  student.number = 100;
  printf("%f %s %d \n", area, person.name, person.number);
  return 0;
}
