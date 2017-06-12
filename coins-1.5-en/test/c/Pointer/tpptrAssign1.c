/* tpptrAssign1.c -- Assignment by using pointer */

int a, b, c;
int *la, *lb;
void printInt(int p);

int main()
{
  la = &a;
  lb = &b;
  a = 1;
  b = 2;
  c = 3;
  *la = *lb;
  *lb = c;
  printInt(a);
  printInt(b);
  return 0;
}

/* SF030620[ */
void printInt(int p)
{printf("%d \n",p);}
/* SF030620] */
