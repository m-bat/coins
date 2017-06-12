/* if statement which includes a non-immediate value on SPARC8 */
int printf(char *s, ...); /* int printf(char *s); SF030514 */

int main() {
	int a, b, c;
	a = 6754;
	b = 1;
	if (a >= 10000) {
		b--;
		printf("a >= 10000\n");
	}
	printf("a,b = %d,%d\n",a,b); /* SF030514 */
	return 0;
}