/* for_07 simple for statement */
             /* two assignment */
/* int printf(char *s, int a,b); */
int printf(char *s, ...); /* SF030514 */

int main() {
        int i,j;
        for (i = 0, j = 10; j > 0; i++, j--)
		printf("i = %d\tj = %d\n", i, j);
	/* } SF030514 */
	return 0;
}