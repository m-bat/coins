/* tpchar1.c:  char, cha*, char** test. */

/* SF030609[ */
void printStr(char *s)
{
  printf("%s\n",s);
}
void printChar(char c)
{
  printf("%c\n",c);
}
void printEol()
{
  printf("\n");
}
/* SF030609] */

char c, *cp;
char ca[10] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j' };
extern void printStr(char* p);
extern void printChar(char p);
extern void printEol();

char f1(char pc[10], char* pcp)
{
  pc[0] = 'x';
  pc[1] = pc[4];
  pc[2] = *(pcp+5);
  return pc[1];
}
char f2(char** papp, char* pcp)
{
  **papp = pcp[3];
  *(*papp+1) = pcp[4];
  *(*papp+2) = pcp[5];
  return *(*papp+2);
}
void print1(char* pName, char pc[])
{
  int i1;

  printStr(pName);
  for (i1 = 0; i1 < 10; i1++)
    printChar(pc[i1]);
  printEol();
}
int main()
{
  char  cb[10];
  char* cap, *cbp, **cbpp;
  int   i;

  print1("ca: ", ca);
  for (i = 0; i < 10; i++)
    cb[i] = ca[i];
  print1("cb: ", cb);
  cb[8] = f1(cb, ca);
  printStr("f1(cb,ca)");
  printChar(cb[8]);
  printEol();
  cbp = &cb[0];
  cb[9] = f1(cbp,ca );
  print1("cb: ", cb);
  cb[9] = f2(&cbp,ca );
  print1("cbp: ", cb);
  return 0;
}

