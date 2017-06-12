/* tpbitfield5.c  small bit fields */

int printf(char*, ...);

int main()
{
  struct bitField1 {
    unsigned a1:1;
    unsigned b1:1;
  };
  struct bitField2 {
    unsigned a1:1;
    unsigned b1:2;
    unsigned char  z1;
  };
  struct bitField3 {
    unsigned char  y1;
    unsigned a1:1;
    unsigned b1:2;
    unsigned char  z1;
  };
  struct bitField4 {
    struct bitField1 st1;
    unsigned char cc1;
    struct bitField1 st2;
    unsigned char cc2;
  };

  union {
    struct bitField1 st1;
    unsigned char cc1;
  } u1;
  union {
    struct bitField2 st1;
    unsigned char    ca1[2];
  } u2;
  union {
    struct bitField3 st1;
    int    ia1[3];
  } u3;
  union {
    struct bitField4 stx;
    unsigned int ia1[4];
  } u4;

/* Clear the unions. (If not, garbage may enter.) */
  u1.cc1 = 0;
  u2.ca1[0] = 0;
  u2.ca1[1] = 0;
  u3.ia1[0] = 0;
  u3.ia1[1] = 0;
  u3.ia1[2] = 0;
  u4.ia1[0] = 0;
  u4.ia1[1] = 0;
  u4.ia1[2] = 0;
  u4.ia1[3] = 0;

/* Set bit fields */
  u1.st1.a1 =  1;
  u1.st1.b1 =  1;
  u2.st1.a1 =  1;
  u2.st1.z1 =  1;
  u2.st1.b1 =  1;
  u3.st1.a1 =  1;
  u3.st1.b1 =  1;
  u3.st1.z1 =  1;
  u4.stx.st1.a1 =  1;
  u4.stx.st1.b1 =  1;
  u4.stx.cc1=  1;
  u4.stx.st2.a1 =  1;
  u4.stx.st2.b1 =  1;
  u4.stx.cc2=  1;
  printf("u1 = %x \n", u1.cc1);  
  printf("u2 = %x %x \n", u2.ca1[0], u2.ca1[1]);  
  printf("u3 = %x %x %x \n", u3.ia1[0], u3.ia1[1], u3.ia1[2]);  
  printf("u4 = %x %x %x %x \n", u4.ia1[0], u4.ia1[1], u4.ia1[2], u4.ia1[3]);  
  printf("sizeof bitField 1=%d 2=%d 3=%d 4=%d \n",
          sizeof(struct bitField1), sizeof(struct bitField2), 
          sizeof(struct bitField3), sizeof(struct bitField4));

/* Result of gcc on Sparc
u1 = c0
u2 = a0 1
u3 = a00100 0 0
u4 = c0000000 1000000 c000000 1000000
sizeof bitFIeld 1=4 2=4 3=4 4=16
*/
/* Result of gcc on x86
u1 = 3
u2 = 3 1
u3 = 10300 0 0
u4 = 3 1 3 1 
sizeof bitFIeld 1=4 2=4 3=4 4=16
*/

}

