int printf(char *s, ...);

int a[1][2][3]
      [1][2][3]
       [1][2][3]
         [1][2][3];

int main() {
  int i1,j1,
       i2,j2,
        i3,j3,
         i4,j4;
  int c=0;
  for(i1=0;i1<2;i1++) for(j1=0;j1<3;j1++)
   for(i2=0;i2<2;i2++) for(j2=0;j2<3;j2++)
    for(i3=0;i3<2;i3++) for(j3=0;j3<3;j3++)
     for(i4=0;i4<2;i4++) for(j4=0;j4<3;j4++) {
        a[0][i1][j1]
          [0][i2][j2]
           [0][i3][j3]
            [0][i4][j4]=++c;
  }
  c=0;
  for(i1=0;i1<2;i1++) for(j1=0;j1<3;j1++)
   for(i2=0;i2<2;i2++) for(j2=0;j2<3;j2++)
    for(i3=0;i3<2;i3++) for(j3=0;j3<3;j3++)
     for(i4=0;i4<2;i4++) for(j4=0;j4<3;j4++) {
        int t;
        t=a[0][i1][j1]
            [0][i2][j2]
             [0][i3][j3]
              [0][i4][j4];
        if(t!=++c) printf("t=%d c=%d\n",t,c);
  }
  printf("Nested array test\n");
  return 0;
}
