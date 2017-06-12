/* function call test(double) */

int printf(char *s, ...);

double func(double a, double b, double c) {
	double r;
	r = a - b;
	return r * c;
}

int main() {
	double a, b, c, result;
	a = 837.89;
	b = 676.32;
	result = func(a, b, 55.2);
	printf("result = %f\n", result);
	return 0;
}
