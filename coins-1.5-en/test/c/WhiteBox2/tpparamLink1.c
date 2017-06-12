/* tpparamLink1.c:  parameter link test */
/*     Kitamura mail 030930 */

int printf(char*, ...);

void bar(int i);
void bar(int i)
{
  printf("%d\n",i);
}

int main()
{
  bar(10);
  return 0;
}

