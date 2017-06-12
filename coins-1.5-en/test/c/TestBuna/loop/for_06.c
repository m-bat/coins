/* for_06 simple for statement */
       /* assignment operator  */
int printf(char *s, ...);

int main() {
        int i;
        for (i = 64; i > 0; i /= 2)
		printf("i = %d\n", i);
	/* } SF030514 */
	return 0;
}