/* problem-020919 from Yamano */
/*   Operation between unsigned int and float is done in double, OK ? */

int printf(char *s, ...);

unsigned int func() {
	return 2147;
}

int main() {
	unsigned int ui;
	float f;
	ui = -555555;
	f = ui;
	printf("f = %f\n", f);

	if (f == ui) {
		printf("f == ui\n");
	} else {
		printf("f != ui\n");
	}

	f = ui - f;
	printf("f = %f\n", f);
	
	f = func();
	printf("f = %f\n", f);
	
	return 0;
}
