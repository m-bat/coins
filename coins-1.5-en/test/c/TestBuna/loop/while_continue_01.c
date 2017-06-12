/* while_continue_01  */
int printf(char *s, ...);

int main() {
	int i;
        int a = 0;
        int b = 0;
        i = -10; /* SF030514 */
	while ( i <10 ) {
          a=a+1;
          if (a==5)
            continue;
          b=b+1;
          i++;
        }
 	printf("a = %d\n", a);
 	printf("b = %d\n", b);
        return 0;
}