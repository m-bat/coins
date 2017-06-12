/* Nested function call test(double) */

double mul_double(double a, double b);
double internal_mul_double(double a, double b);
int printf(char *s, ...);

double a;

int main() {
	double result;
	a = 399.32;
	result = mul_double(88.8, a);
	printf("result = %4.8f\n", result);
	return 0;
}

double mul_double(double a, double b) {
	return internal_mul_double(a, b);
}

double internal_mul_double(double a, double b) {
	return a * b;
}
