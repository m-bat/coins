/* operation of two non-immediate values on SPARC8 */
int printf(char *s, ...);

int main() {
	int a, b;
	a = 3284;
	b = 38375 + a - 473738;
	printf("b = %d\n", b);
	return 0;
}