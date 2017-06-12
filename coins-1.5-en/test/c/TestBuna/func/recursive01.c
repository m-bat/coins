/* Recursive function call */
int factorial_of(int n);
int printf(char *s, ...);

int main(int argc, char **argv)
{
	int result;
	result = factorial_of(5);
	printf("result = %d\n", result);
	return 0;
}

int factorial_of(int n)
{
	if (n == 1) {
		return 1;
	}
	return n * factorial_of(n - 1);
}