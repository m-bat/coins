int printf(char *s, ...);

int main() {
	int array[10];
	int a;
	array[0] = 0;
	array[1] = 10;
	array[2] = 30;
	a = array[2];
	
	printf("a = %d\n", a);
	return 0;
}