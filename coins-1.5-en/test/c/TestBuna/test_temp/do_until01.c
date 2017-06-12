int printf(char *s, ...);

int main() {
	int j;
	j= 1;
	do {
	  j = j + 1;
	} while (j <= 8);
	printf("j = %d\n", j);
	return 0;
}