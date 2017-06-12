/* tpInitErr.c */

int hoge()
{
	return 20;
}

int a = 10 + hoge();

int main(int argc, char * argv[])
{
  printf("a=%d\n", a);
  return 0;
}
