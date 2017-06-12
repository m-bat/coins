/* switch test with "default", "break" */

/*int printf(char *s, ...); */

int main() {
	int i;
	int result;
	result = 0;
	i = 3;
	switch (i) {
		case 1:
			result = 11;
		case 3:
			result = 1;
		case 9:
			result = 999;
	}
	printf("result = %d\n", result);
	return 0;
}
