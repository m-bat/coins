/* tpbitfield3.c  */

int printf(char*, ...);

int main()
{
  int i; 

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

  union {
    struct bitField1 st1;
    int  a1[3];
  } u1;
  union {
    struct bitField2 st2;
    int    a2[10];
  } u2;
  union {
    struct bitField3 st3;
    int    a3[10];
  } u3;
  union {
    struct bitField4 st4;
    int    a4[10];
  } u4;

/* Clear the unions. (If not, garbage may enter.) */
  u1.a1[0] = 0;
  u1.a1[1] = 0;
  u1.a1[2] = 0;
  for (i = 0; i < 10; i++) {
    u2.a2[i] = 0;
    u3.a3[i] = 0;
    u4.a4[i] = 0;
  }

/* Set bit fields */
  u1.st1.x1 =  1;
  u1.st1.y1 = '0';
  u1.st1.a1 = 15;
  u1.st1.b1 =  1;
  u1.st1.c1 =  3;
  u1.st1.z1 =  1;
  u2.st2.x2 =  1;
  u2.st2.a2 =  1;
  u2.st2.b2 =  1;
  u2.st2.c2 =  1;
  u2.st2.d2 =  1;
  u2.st2.e2 =  1;
  u2.st2.f2 =  1;
  u2.st2.g2 =  1;
  u2.st2.h2 =  1;
  u2.st2.i2 =  1;
  u2.st2.j2 =  1;
  u2.st2.k2 =  1;
  u2.st2.l2 =  1;
  u2.st2.m2 =  1;
  u2.st2.n2 =  1;
  u2.st2.z2 =  1;
  u3.st3.x3 =  1;
  u3.st3.a3 =  1;
  u3.st3.b3 =  1;
  u3.st3.c3 =  1;
  u3.st3.d3 =  1;
  u3.st3.e3 =  1;
  u3.st3.f3 =  1;
  u3.st3.g3 =  1;
  u3.st3.h3 =  1;
  u3.st3.i3 =  1;
  u3.st3.j3 =  1;
  u3.st3.k3 =  1;
  u3.st3.l3 =  1;
  u3.st3.m3 =  1;
  u3.st3.n3 =  1;
  u3.st3.z3 =  1;
  u4.st4.x4 =  1;
  u4.st4.a4 =  1;
  u4.st4.b4 =  1;
  u4.st4.c4 =  1;
  u4.st4.d4 =  1;
  u4.st4.e4 =  1;
  u4.st4.f4 =  1;
  u4.st4.g4 =  1;
  u4.st4.h4 =  1;
  u4.st4.i4 =  1;
  u4.st4.j4 =  1;
  u4.st4.k4 =  1;
  u4.st4.l4 =  1;
  u4.st4.m4 =  1;
  u4.st4.n4 =  1;
  u4.st4.z4 =  1;
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
  printf("sizeof bitField 1=%d 2=%d 3=%d 4=%d \n",
          sizeof(struct bitField1), sizeof(struct bitField2), 
          sizeof(struct bitField3), sizeof(struct bitField4));

/* Result of gcc on Sparc
u1 = 1307c 60010000 0 
u2 = 1a44208 2020100 400800 100080 40100 0 0 0 0 0 1307c 60010000 0 ffbffbe4 4 a 
u3 = 1000200 40020 100200 402020 20844a0 13f7844 0 ff33c000 0 0 
u4 = 1a450 82058 1009804 400be0 100080 50140 ffbffb20 ff29ca78 0 0 
sizeof bitField 1=8 2=20 3=24 4=24 
*/

/* Result of gcc on x86
u1 = 2f300001 103 0 
u2 = 844b01 8081 401 1001 10001 0 0 0 0 0 22f068 77e5b3db 2f300001 103 0 6109106a 
u3 = 101 4001 1001 80401 2442081 1 0 0 0 0 
u4 = 4b0001 82100 101 401 1001 10001 0 0 0 0 
sizeof bitField 1=8 2=20 3=24 4=24 
*/

}
