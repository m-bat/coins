/* tpAlias1.c  Alias test */

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

int *f1( int pa[10], int *pi)
{
  int i;
  for (i = 1; i < 10; i++)
    pa[i] = pa[i-1] + *pi;
  return &pa[0];
}

float f2( struct point ppoint )
{
  return ppoint.x1 * ppoint.y1;
}

int main()
{
  int i, j, k, m, n;
  int x, y, xx, yy, zz;
  int *px, *py;
  int a[30];
  struct point lPoint = { 2.0, 3.0 };
  struct nameList person = {"tanaka", 99};
  float area;
  j = 0;
  n = 30;
  m = 20;
  xx = 3;
  a[0] = 1;
  a[1] = 1;
  for (i = 2; i < n; i++) {
    a[i] = a[i-1] + a[i-2];
  }
  a[j] = a[n-1];

  x = 0;
  for (i = 0; i < n; i++) {
    a[i] = xx;
    for (j = i+1; j < m; j++) {
      a[i] = a[i] + a[j];
    }
    x = x + a[i];
  }
  y = a[0];

  for (i=0; i<n; i++) {
    x = a[i];
    if (i % 2 == 0) 
      a[i+1] = y ;
    else
      a[i] = x+1 ; 
  }

  zz = 5;
  px = &xx;
  *px = 1;
  px = &yy;
  *px = 2;
  px = f1(ga, px);
  y = *px;
  printf("%d %d %d %d %d \n", x, y, xx, yy, *px);
  gpoint.x1 = 3.0;
  gpoint.y1 = 4.0;
  area = f2(gpoint);
  student.name = "suzuki";
  student.number = 100;
  printf("%f %s %d \n", area, person.name, person.number);
  return 0;
}
