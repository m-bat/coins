/* tpinit13.c:  global variable initiation in most simple form */
/*     Nakata mail 040525 */

int printf(char*, ...);

int GlobalVariable = 10; 
int g2;

int main()
{
  printf("%d\n",GlobalVariable);
  return 0;
}

