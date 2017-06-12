/* switch test without "default" */

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
		case 3:
			result = 1;
	}
	printf("result = %d\n", result);
	return 0;
}
