int printf(char *s, ...);

int main() {
	int array[5000];
	array[4001] = 22;
	
	printf("array[4001] = %d\n", array[4001]);
	return 0;
}