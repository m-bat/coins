/* Function call strlen */
int strlen(char *s);
int printf(char *s, ...);

int main(int argc, char **argv) {
	int i, len;
	/* for (i = 0; i < argc; i++) { */
	for (i = 1; i < argc; i++) { /* SF030514 */
		len = strlen(argv[i]);
		printf("len[%d] = %d\n", i, len);
	}
	return 0;
}