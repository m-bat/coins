/* switch test without "default" */

/*int printf(char *s, ...); */

int main() {
	int i;
	int result;
	result = 0;
	i = 3;
	switch (i) {
		case 1:
			result = 0;
		case 8081:
			result = 8081;
		case 5:
			result = 1;
	}
	printf("result = %d\n", result);
	return 0;
}
