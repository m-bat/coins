/* function call test(array) */

int printf(char *s, ...);

int func(int a[], int b[]) {
	int result;
	result = a[0] + b[1];
	return result;
}

int main() {
	int arr1[5];
	int arr2[3];
	int i;
	int result;
	for (i = 0; i < 5; i++) {
		arr1[i] = i * 5;
	}
	for (i = 0; i < 3; i++) {
		arr2[i] = i * 11;
	}
	result = func(arr1, arr2);
	printf("result = %d\n", result);
	return 0;
}