/* jisProto1.c : JIS C 6.1.2.5, 6.1.2.6 Prototype pp.25-26 */

struct tag {int x, y;};

int g(struct tag (*[5])(float));
int f(int (*)(), double (*)[3]);
int f(int (*)(char *), double (*)[]);
int f(int (*)(char *), double (*)[3]);

int 
main()
{
  /* SF030620[ */
  printf("jisProto1.c : JIS C 6.1.2.5, 6.1.2.6 Prototype pp.25-26\n");
  /* SF030620] */
  return 0;
}

