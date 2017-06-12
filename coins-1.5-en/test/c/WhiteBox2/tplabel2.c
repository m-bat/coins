/* tplabel2.c:  label check  */
/*   Automaton interpreter for Pascal string */

int printf(char*, ...);

int main()
{
  int i, j;
  char a[10] = {'\'', 'd', 'o', 'n', '\'', '\'', 't', '\'', '\0'};
  char x;
  char quote= '\'';
  char end  = '\0';
  i = 0;
  x = a[i++];
  if (x==quote) L1:{
    x=a[i++];
    if ((x != quote)&&(x != end)) {
     L2:
      while ((x != quote)&&(x != end)) {
        x = a[i++];
      }
      if (x == quote) {
        x = a[i++];
        goto L3;
      }else goto L9;
    }else if (x == quote) {
      x = a[i++];
      goto L3;
    }else if (x != end)
      goto L9;
   L3:
    if (x == quote) {
      x = a[i++];
      goto L2;
    }else if (x == end) {
      printf("accepted\n");
      return 0;
    }else goto L9;
   L9:
    printf("error\n");
    return 0;
  }else goto L9;
}

