/* for_continue_01 nest for statement */
int printf(char *s, ...);

int main() {
	int i;
        int a,b;
        a = b = 0; /* int a = b = 0; SF030514 */
	for (i = 0; i <10; i++) {
          a=a+1;
          if (a==5)
            continue;
          b=b+1;
        }
 	printf("a = %d\n", a);
 	printf("b = %d\n", b);
        return 0;
}