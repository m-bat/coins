int printf(char *s, ...);

int main(int argc, char **argv) {
	int i;
	/* for (i = 0; i < argc; i++) { */
	for (i = 1; i < argc; i++) { /* SF030514 */
		printf("argv[%d] = %s\n", i, argv[i]);
	}
	return 0;
}