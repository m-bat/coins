/* Call strcpy */

char *strcpy(char *s1, char *s2);
int strlen(char *s);
int printf(char *s, ...);
int puts(char *s);

#define LENGTH 50

int main(int argc, char **argv) {
	char a[LENGTH], b[LENGTH];
	int i, len;
	strcpy(a, "hogehoge");
	strcpy(b, a);
	puts(a);
	len = strlen(b);
	for (i = 0; i < len; i++) {
		printf("%c ", i, b[i]);
	}
	printf("\n");
	return 0;
}