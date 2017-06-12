/* Nested function call(char) */

char internal_add_char(char a, char b);
char add_char(char a, char b);
int printf(char *s, ...);

int main(int argc, char **argv) {
	char a, b, c;
	a = -7;
	b = add_char(a, 23);
	printf("b = %d\n", b);
	return 0;
}

char internal_add_char(char a, char b) {
	char r;
	r = a + b;
	return r;
}

char add_char(char a, char b) {
	return internal_add_char(a, b);
}
