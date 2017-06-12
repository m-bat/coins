/* operation of including a non-immediate value on SPARC8 */
int printf(char *s, ...);

int main() {
	int a, b;
	a = 32;
	b = a + 58433;
	printf("b = %d\n", b);
	return 0;
}