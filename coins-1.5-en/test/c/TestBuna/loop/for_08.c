/* for_08 simple for statement */
             /* function call */
int printf(char *s, ...);

int main() {
        int i;
        for (i = 0; i < 10; printf("i = %d\n", i))
		i++;
 	/* } SF030514 */
	return 0;
}