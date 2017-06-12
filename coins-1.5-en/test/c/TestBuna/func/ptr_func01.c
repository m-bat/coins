/* function call test(pointer) */

int printf(char *s, ...);

float func(float *a, float *b) {
	float result;
	result = *a + *b;
	return result;
}

int main() {
	float a, b;
	float *a_p, *b_p;
	float result;
	a = 53.33;
	b = 4344.434;
	a_p = &a;
	b_p = &b;
	result = func(a_p, b_p);
	printf("result = %5.5f\n", result);
	return 0;
}