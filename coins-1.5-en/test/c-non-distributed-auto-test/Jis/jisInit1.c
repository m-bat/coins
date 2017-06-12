/* jisInit1.c : JIS C 6.5.7 Initiation pp.75-77 */

#include <stdio.h>

  int x[] = {1, 3, 5};
  float y1[4][3] = {{1,3,5},{2,4,6},{3,5,7}, };
  float y2[4][3] = { 1,3,5, 2,4,6, 3,5,7 };
  float z[4][3]  = {{1},{2},{3},{4}};
  struct { int a[3], b; } w0 = {{1,11,111},2}; /**/
  struct { int a[3], b; } w[] = {{1},2};
  short q1[4][3][2] = {{1},{2,3},{4,5,6}};
  short q2[4][3][2] = {
          1,0,0,0,0,0,
          2,3,0,0,0,0,
          4,5,6};
  short q3[4][3][2] = {
         { {1}, },
         { {2,3}, },
         { {4,5}, {6}, } };
  typedef int A[];
  A a3, b3;
  A a1 = {1,2}, b1 = {3,4,5};
  int a2[] = {1,2}, b2[] = {3,4,5};
  char s1[] = "abc", t1[4] = "abc";  /* t1[3] = "abc"; */
  char s2[] = {'a','b','c','\0'},
       t2[] = {'a','b','c'};
  char *p = "abc";

int 
main()
{
  int i, j;

  printf("x %d %d %d \n", x[0], x[1], x[2]);
  for (i = 0; i < 4; i++) {
    printf("y1[%d]= %f %f %f \n", i, y1[i][0],y1[i][1],y1[i][2]);
  }
  for (i = 0; i < 4; i++) {
    printf("y2[%d]= %f %f %f \n", i, y2[i][0],y2[i][1],y2[i][2]);
  }
  for (i = 0; i < 4; i++) {
    printf("z[%d]= %f %f %f \n", i, z[i][0],z[i][1],z[i][2]);
  }
  for (i = 0; i < 2; i++) {
    printf("struct w[%d].a= %d %d %d b %d\n", 
           i, w[i].a[0],w[i].a[1],w[i].a[2],w[i].b);
  }
  for (i = 0; i < 4; i++) {
    printf("q1[%d][0-2][0-1]= %d %d   %d %d  %d %d\n", 
           i, q1[i][0][0],q1[i][0][1],
              q1[i][1][0],q1[i][1][1], 
              q1[i][2][0],q1[i][2][1]); 
  }
  for (i = 0; i < 4; i++) {
    printf("q2[%d][0-2][0-1]= %d %d   %d %d  %d %d\n", 
           i, q2[i][0][0],q2[i][0][1],
              q2[i][1][0],q2[i][1][1], 
              q2[i][2][0],q2[i][2][1]); 
  }
  for (i = 0; i < 4; i++) {
    printf("q3[%d][0-2][0-1]= %d %d   %d %d  %d %d\n", 
           i, q3[i][0][0],q3[i][0][1],
              q3[i][1][0],q3[i][1][1], 
              q3[i][2][0],q3[i][2][1]); 
  }
  printf("a1 %d %d  b1 %d %d %d\n", a1[0],a1[1], b1[0],b1[1],b1[2]);
  printf("a2 %d %d  b2 %d %d %d\n", a2[0],a2[1], b2[0],b2[1],b2[2]);
  printf("s1 %c %c %c 0x%x t1 %c %c %c \n",  /**/
          s1[0],s1[1],s1[2],s1[3], t1[0],t1[1],t1[2]);
  printf("s2 %c %c %c 0x%x t2 %c %c %c \n", /**/
          s2[0],s2[1],s2[2],s2[3], t2[0],t2[1],t2[2]);
  printf("p %s \n", p);
  return 0; 
}

