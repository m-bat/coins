/* for_02 simple for statement */
         /*  no init statement */
int printf(char *s, ...);

int main() {
	int a, i;
        a = 0;
        i = 0;
	for (; i < 5; i++) {
		a += 10;
		printf("i = %d\n", i);
                printf("a = %d\n", a);
	}
	return 0;
}