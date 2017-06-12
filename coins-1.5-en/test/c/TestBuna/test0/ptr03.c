int printf(char *s, ...);

int main() {
	int a, b;
	int *a_ptr1;
	int **a_ptr2;
	a = 555;
	a_ptr1 = &a;
	a_ptr2 = &a_ptr1;
	b = **a_ptr2;
	printf("a_ptr1 = %d\n", *a_ptr1);
	printf("a_ptr2 = %d\n", **a_ptr2);
	printf("b = %d\n", b);
	return 0;
}