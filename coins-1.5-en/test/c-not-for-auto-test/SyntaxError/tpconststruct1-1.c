/* tpconststruct1-1.c:  const struct test */
/*   COINS: compile error, gcc: warning */

int printf(char*, ...);

int a, b, c;
struct class {
  int num;
  const struct class *others;
  const void **strlist; 
};
struct class1 {
  int num;
  struct class1 *others;
  void **strlist; 
};
struct class st0 = { 99, 0, 0 };
struct class st1 = { 11, &st0, 0 };
int ii = 22;
const struct class st2 = { 33, &st1, &ii }; /* excess initializers */
const struct class st3 = { 33, &st1, &st1 }; /* excess initializers */

int main()
{
  struct class st11 = { 44, &st2, &ii };
  int jj = 55;
  const struct class st12 = { 66, &st11, &jj }; /* excess initializers */
  struct class1 st20 = { 35, 0, 0};
  struct class1 st21;
  struct class1 st22;

  st1.strlist = &jj;
  st21 = st20;
  st22 = st21;
  
  printf("st1 = {%d %d %d }\n", st1.num, st1.others->num, *st1.strlist);
  printf("st2 = {%d %d %d }\n", st2.num, st2.others->num, *st2.strlist);
  printf("st11 = {%d %d %d }\n", st11.num, st11.others->num, *st11.strlist);
  printf("st12 = {%d %d %d }\n", st12.num, st12.others->num, *st12.strlist);
  return 0;
}
