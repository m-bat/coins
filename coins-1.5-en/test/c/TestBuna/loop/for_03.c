/* for_03 simple for statement */
         /*  two init statement */
int printf(char *s, ...);

int main() {
	int a, i;
	for (a = 0,i = 0; i < 5; i++) {
		a += 10;
		printf("i = %d\n", i);
                printf("a = %d\n", a);
	}
	return 0;
}