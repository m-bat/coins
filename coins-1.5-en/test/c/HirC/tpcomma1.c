/* tpcomma1.c  comma operator test */

int  i, j, k;
char a[100], b[50];

void reverse(char s[], int n)  /* K & R p. 76. */
{
  int  i, j;
  char c;

  for (i = 0, j = n-1; i < j; i++, j--) {
    c = s[i];
    s[i] = s[j];
    s[j] = c;
  }
  /* SF030620[ */
  printf("reverse(s[%d %d %d %d %d %d %d %d %d %d],n=%d)\n"
	 ,s[0],s[1],s[2],s[3],s[4],s[5],s[6],s[7],s[8],s[9],n);
  /* SF030620] */
}

int dummy() { return 1; }

int main()
{
  k = (i + 1, dummy(), 10 + j);
  printf("i=%d j=%d k=%d\n",i,j,k); /* SF030620 */
  reverse(a, (i = 0, j = 10)); /* <- how does this work?? */
  printf("NOT YET!!\n");
  return 0; 
}

