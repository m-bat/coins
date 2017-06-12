/* switch test with "default", "break" */

/*int printf(char *s, ...); */

int main() {
	int i;
	int result;
	result = 0;
	i = 3;
	switch (i) {
		case 3:
			result = 1;
			break;
		case 1:
			result = 38;
			break;
		case 9:
			result = 99;
			break;
		default:
			result = -1;
			break;
	}
	printf("result = %d\n", result);
	return 0;
}
