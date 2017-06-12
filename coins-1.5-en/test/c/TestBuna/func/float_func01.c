/* function call test(float) */

int printf(char *s, ...);

float func(float a, float b) {
	float r;
	r = a / b;
	return r;
}

int main() {
	float a, b, c, result;
	a = 837.89;
	result = func(a, 11.11F);
	printf("result = %5.6f\n", result);
	return 0;
}
