/* if statement */
int printf(char *s, ...);

int main() {
	int a, b, c;
	a = 1338;
	b = 0;
	if (6746 <= a) {
		b++;
	} else if (a > 4002) {
		b--;
	}
	printf("b = %d\n", b);
	return 0;
}