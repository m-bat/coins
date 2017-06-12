/* automata1.c:  label check  */
/*   automaton interpreter for Pascal string constant. */

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
    printf("L1quote  %c\n", x);
    x=a[i++];
    if ((x != quote)&&(x != end)) {
     L2:
      printf("L2c      %c\n", x);
      while ((x != quote)&&(x != end)) {
        printf("L2whilec %c\n", x);
        x = a[i++];
      }
      if (x == quote) {
        printf("L2quote  %c\n", x);
        x = a[i++];
        goto L3;
      }else goto L9;
    }else if (x == quote) {
      printf("L2quote2 %c \n", x);
      x = a[i++];
      goto L3;
    }else if (x != end)
      goto L9;
   L3:
    if (x == quote) {
      printf("L3quote  %c \n", x);
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

