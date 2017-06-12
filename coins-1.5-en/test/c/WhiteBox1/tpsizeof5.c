/* Sizeof/tpsizeof5.c  from KandR/6-3.c */

struct key
{
	char *word;
	int count;
} keytab[] =
{
	"auto", 0,
	"break", 0,
	"case", 0,
	"char", 0,
	"while", 0
};

#define NKEYS  (sizeof keytab / sizeof keytab[0])

main()
{
  int n;
  n = NKEYS;  /* 5 */
  printf("%d \n", n);
  return 0;
}

