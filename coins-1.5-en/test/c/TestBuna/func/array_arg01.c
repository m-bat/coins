/* Can pointer be an argument? */

int printf(char *s, ...);

int get_total(int a[5]) {
	int i, result;
	result = 0;
	for (i = 0; i < 5; i++) {
		result += i;
	}
	return result;
}

int main() {
	int array[5];
	int i, total;
	for (i = 0; i < 5; i++) {
		array[i] = i * 5000;
	}
	total = get_total(array);
	printf("total = %d\n", total);
	return 0;
}