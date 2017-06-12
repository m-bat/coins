/* tpbitfield1.c  Hasegawa 031127 */

int printf(char*, ...);

int main()
{
  union {
    struct {
      unsigned a:7;
      unsigned c:2;
    } s;
    struct {
      unsigned char c;
      unsigned char d;
    } t;
  } u;
  u.t.c = 0;
  u.t.d = 0;
  u.s.c = 3;
  printf("u.t.c = %d\n", u.t.c);    /*   1 */
  printf("u.t.d = %d\n", u.t.d);    /* 128 */
  printf("25165824=%x\n", 25165824);/* 18000000 */
}
