/* tpbitfield2.c  Mori mail 041111 */

int printf(char*, ...);

int main()
{
  int i; 

  struct intField1 {
    short x1;
    char  y1;
    unsigned a1:5;
    unsigned b1:1;
    unsigned c1:3;
    char  z1;
  };
  struct intField2 {
    short x2;
    short y2;
    unsigned a2:5;
    unsigned b2:1;
    unsigned c2:3;
    char  z2;
  };
  struct charField1 {
    char x3;
    unsigned char a3:5;
    unsigned char b3:1;
    unsigned char c3:3;
    char  z3;
  };
  struct shortField1 {
    char x4;
    char x4a;
    unsigned short a4:5;
    unsigned short b4:1;
    unsigned short c4:3;
    char  z4;
  };
  struct mixedField1 {
    char x5;
    unsigned char  a5:5;
    unsigned int   b5:5;
    unsigned char  c5:2;
    unsigned short d5:3;
    unsigned char  e5:2;
    unsigned int   f5:5;
    char  z5;
  };

  union {
    struct intField1 st1;
    int  a1[3];
  } u1;
  union {
    struct intField2 st2;
    int    a2[3];
  } u2;
  union {
    struct charField1 st3;
    int    a3[3];
  } u3;
  union {
    struct shortField1 st4;
    int    a4[3];
  } u4;
  union {
    struct mixedField1 st5;
    int    a5[6];
  } u5;

/* Clear the unions. (If not, garbage may enter.) */
  u1.a1[0] = 0;
  u1.a1[1] = 0;
  u1.a1[2] = 0;
  u2.a2[0] = 0;
  u2.a2[1] = 0;
  u2.a2[2] = 0;
  u3.a3[0] = 0;
  u3.a3[1] = 0;
  u3.a3[2] = 0;
  u4.a4[0] = 0;
  u4.a4[1] = 0;
  u4.a4[2] = 0;
  for (i = 0; i < 6; i++)
    u5.a5[i] = 0;

/* Set bit fields */
  u1.st1.x1 =  1;
  u1.st1.y1 = '0';
  u1.st1.a1 = 15;
  u1.st1.b1 =  1;
  u1.st1.c1 =  3;
  u1.st1.z1 =  1;
  u2.st2.x2 =  1;
  u2.st2.y2 =  1;
  u2.st2.a2 = 15;
  u2.st2.b2 =  1;
  u2.st2.c2 =  3;
  u2.st2.z2 =  1;
  u3.st3.x3 =  1;
  u3.st3.a3 = 15;
  u3.st3.b3 =  1;
  u3.st3.c3 =  3;
  u3.st3.z3 =  1;
  u4.st4.x4 =  1;
  u4.st4.a4 = 15;
  u4.st4.b4 =  1;
  u4.st4.c4 =  3;
  u4.st4.z4 =  1;
  u5.st5.x5 =  1;
  u5.st5.a5 = 15;
  u5.st5.b5 =  1;
  u5.st5.c5 =  3;
  u5.st5.d5 =  3;
  u5.st5.e5 =  3;
  u5.st5.f5 =  3;
  u5.st5.z5 =  1;
  printf("u1 = %x %x %x \n", u1.a1[0], u1.a1[1], u1.a1[2]);  
  printf("u2 = %x %x %x \n", u2.a2[0], u2.a2[1], u2.a2[2]);  
  printf("u3 = %x %x %x \n", u3.a3[0], u3.a3[1], u3.a3[2]);  
  printf("u4 = %x %x %x \n", u4.a4[0], u4.a4[1], u4.a4[2]);  
  printf("u5 = %x %x %x %x %x %x \n", u5.a5[0], u5.a5[1], u5.a5[2],
                 u5.a5[3], u5.a5[4], u5.a5[5]);  
  printf("sizeof bitField 1=%d 2=%d 3=%d 4=%d 5=%d \n",
          sizeof(struct intField1), sizeof(struct intField2), 
          sizeof(struct charField1), sizeof(struct shortField1),
          sizeof(struct mixedField1));

/* Result of gcc on Sparc

u1 = 1307c 60010000 0 
 
u2 = 10001 7d800100 0 

u3 = 17c6001 0 0 

u4 = 17c6001 0 0 

u5 = 17876c6 1000000 0 0 0 0 

*/

/* Result of gcc on x86

u1 = 2f300001 103 0 

u2 = 10001 100ef 0 

u3 = 1032f01 0 0 

u4 = 1032f01 0 0 

u5 = f3c2f01 1 0 0 0 0 

*/

}
