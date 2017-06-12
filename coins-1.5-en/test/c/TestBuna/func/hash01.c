int hash(char *s);
int printf(char *s, ...);
void exit(int a);

int main(int argc, char **argv)
{
	int code;
	if (argc == 1) {
		printf("Input something\n");
		exit(1);
	}
	code = hash(argv[0]);
	printf("code = %d\n", code);
}

int hash(char *s)
{
	int i;
	i = 0;
	while (*s) {
		i += *s++;
	}
	return i % 100;
}