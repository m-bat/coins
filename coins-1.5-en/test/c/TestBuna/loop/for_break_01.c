/* for_break_01 nest for statement */
int printf(char *s, ...);

int main() {
	int i;
        int a = 0;
	for (i = 0; i <10; i++) {
          a=a+1;
          if (a==5)
            break;
          }
 	printf("a = %d\n", a);
	return 0;
}