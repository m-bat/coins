/* Nested function call test(float) */

float mul_float(float a, float b);
float internal_mul_float(float a, float b);
int printf(char *s, ...);

float a;

int main() {
	float result;
	a = 399.32;
	result = mul_float(88.8, a);
	printf("result = %4.8f\n", result);
	return 0;
}

float mul_float(float a, float b) {
	return internal_mul_float(a, b);
}

float internal_mul_float(float a, float b) {
	return a * b;
}
