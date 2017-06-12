/* assigning a non-immediate value on SPARC8 */
int printf(char *s, ...);

int main() {
	int a, b;
	a = 38375;
	b = a + 1;
	printf("b = %d\n", b);
	return 0;
}