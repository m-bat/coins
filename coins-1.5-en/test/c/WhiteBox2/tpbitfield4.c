/* tpbitfield4.c   Alignment for global variables */

int printf(char*, ...);

  struct bitField1 {
    short x1;
    char  y1;
    unsigned a1:5;
    unsigned b1:1;
    unsigned c1:3;
    char  z1;
  };
  struct bitField2 {
    char x2;
    unsigned a2:1;
    unsigned b2:2;
    unsigned c2:3;
    unsigned d2:4;
    unsigned e2:5;
    unsigned f2:6;
    unsigned g2:7;
    unsigned h2:8;
    unsigned i2:9;
    unsigned j2:10;
    unsigned k2:11;
    unsigned l2:12;
    unsigned m2:13;
    unsigned n2:14;
    char  z2;
  };
  struct bitField3 {
    char x3;
    unsigned a3:15;
    unsigned b3:14;
    unsigned c3:13;
    unsigned d3:12;
    unsigned e3:11;
    unsigned f3:10;
    unsigned g3:9;
    unsigned h3:8;
    unsigned i3:7;
    unsigned j3:6;
    unsigned k3:5;
    unsigned l3:4;
    unsigned m3:3;
    unsigned n3:2;
    char  z3;
  };
  struct bitField4 {
    short x4;
    unsigned a4:1;
    unsigned b4:2;
    unsigned c4:3;
    unsigned d4:4;
    char x4a;
    unsigned e4:5;
    unsigned f4:6;
    unsigned g4:7;
    unsigned h4:8;
    unsigned i4:9;
    char x4b;
    unsigned j4:10;
    unsigned k4:11;
    unsigned l4:12;
    unsigned m4:13;
    unsigned n4:14;
    char  z4;
  };

  struct {
    char cc1;
    struct bitField1 st1;
    char cc2;
  } s1;
  struct {
    char cc1;
    struct bitField2 st2;
    char cc2;
  } s2;
  struct {
    char cc1;
    struct bitField3 st3;
    char cc2;
  } s3;
  struct {
    char cc1;
    struct bitField4 st4;
    char cc2;
  } s4;

int main()
{
  int i; 



/* Set bit fields */
  s1.st1.x1 =  1;
  s1.st1.y1 = '0';
  s1.st1.a1 = 15;
  s1.st1.b1 =  1;
  s1.st1.c1 =  3;
  s1.st1.z1 =  1;
  s2.st2.x2 =  1;
  s2.st2.a2 =  1;
  s2.st2.b2 =  1;
  s2.st2.c2 =  1;
  s2.st2.d2 =  1;
  s2.st2.e2 =  1;
  s2.st2.f2 =  1;
  s2.st2.g2 =  1;
  s2.st2.h2 =  1;
  s2.st2.i2 =  1;
  s2.st2.j2 =  1;
  s2.st2.k2 =  1;
  s2.st2.l2 =  1;
  s2.st2.m2 =  1;
  s2.st2.n2 =  1;
  s2.st2.z2 =  1;
  s3.st3.x3 =  1;
  s3.st3.a3 =  1;
  s3.st3.b3 =  1;
  s3.st3.c3 =  1;
  s3.st3.d3 =  1;
  s3.st3.e3 =  1;
  s3.st3.f3 =  1;
  s3.st3.g3 =  1;
  s3.st3.h3 =  1;
  s3.st3.i3 =  1;
  s3.st3.j3 =  1;
  s3.st3.k3 =  1;
  s3.st3.l3 =  1;
  s3.st3.m3 =  1;
  s3.st3.n3 =  1;
  s3.st3.z3 =  1;
  s4.st4.x4 =  1;
  s4.st4.a4 =  1;
  s4.st4.b4 =  1;
  s4.st4.c4 =  1;
  s4.st4.d4 =  1;
  s4.st4.e4 =  1;
  s4.st4.f4 =  1;
  s4.st4.g4 =  1;
  s4.st4.h4 =  1;
  s4.st4.i4 =  1;
  s4.st4.j4 =  1;
  s4.st4.k4 =  1;
  s4.st4.l4 =  1;
  s4.st4.m4 =  1;
  s4.st4.n4 =  1;
  s4.st4.z4 =  1;
/*
  printf("u1 = %x %x %x \n", u1.a1[0], u1.a1[1], u1.a1[2]);  
  printf("u2 = %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x \n",
          u2.a2[0], u2.a2[1], u2.a2[2], u2.a2[3], u2.a2[4], u2.a2[5],
          u2.a2[6], u2.a2[7], u2.a2[8], u2.a2[9], u2.a2[10], u2.a2[11],
          u2.a2[12], u2.a2[13], u2.a2[14], u2.a2[15]);  
  printf("u3 = %x %x %x %x %x %x %x %x %x %x \n",
          u3.a3[0], u3.a3[1], u3.a3[2], u3.a3[3], u3.a3[4], u3.a3[5],
          u3.a3[6], u3.a3[7], u3.a3[8], u3.a3[9]);
  printf("u4 = %x %x %x %x %x %x %x %x %x %x \n",
          u4.a4[0], u4.a4[1], u4.a4[2], u4.a4[3], u4.a4[4], u4.a4[5],
          u4.a4[6], u4.a4[7], u4.a4[8], u4.a4[9]);
*/
  printf("sizeof bitField 1=%d 2=%d 3=%d 4=%d \n",
          sizeof(struct bitField1), sizeof(struct bitField2), 
          sizeof(struct bitField3), sizeof(struct bitField4));
  printf("sizeof struct 1=%d 2=%d 3=%d 4=%d \n",
          sizeof(s1), sizeof(s2), 
          sizeof(s3), sizeof(s4));

/* Result of gcc on Sparc
sizeof bitField 1=8 2=20 3=24 4=24 
sizeof struct 1=16 2=28 3=32 4=32 
*/

/* Result of gcc on x86
sizeof bitField 1=8 2=20 3=24 4=24 
sizeof struct 1=16 2=28 3=32 4=32 
*/

}
