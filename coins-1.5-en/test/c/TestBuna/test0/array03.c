int printf(char *s, ...);

int main() {
	int array[10], a;
	a = 5;
	array[a+3] = 80;
	
	printf("array[a+3] = %d\n", array[a+3]);
	return 0;
}
