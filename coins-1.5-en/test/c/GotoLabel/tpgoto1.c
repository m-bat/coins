/* tpgoto1.c:  goto statement in if statement */

int printf(char *, ...);

int main()
{
  int c, i, j;
  j = 10;
  i = -8;
 L1: 
  i = i + 1;
 L2:
  j = j + 1;
  c = (j < 10);
  if (c)
    goto L1;
  j = j / 2;
  c = (j < 8);
  if (c)
    goto L3;
  i = 2;
  goto L1;
 L3:
  printf("i=%d j=%d \n", i, j); /* SF030620 */
  return 0;
} 

