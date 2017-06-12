/* switch test with "default", without "break" */

/*int printf(char *s, ...); */

int main() {
	int i;
	int result;
	result = 0;
	i = 3;
	switch (i) {
		case 1:
			result = 11;
		case 2:
			result = 22;
		case 9999:
			result = 1;
		default:
			result = -1;
	}
	printf("result = %d\n", result);
	return 0;
}
