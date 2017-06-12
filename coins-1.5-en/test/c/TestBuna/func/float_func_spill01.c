/* Argument spill test(float) */
int printf(char *s, ...);

float sub(
	float a, float b, float c, float d, float e, float f,
	float g, float h
) {
	float res;
	res = a - b - c - d - e - f - g - h;
	return res;
}

int main() {
	float aa, bb, cc, dd, ee;
	double result;
	aa = 1.0F;
	bb = 2.0F;
	cc = 3.0F;
	dd = 4.0F;
	ee = 5.0F;
	result = sub(aa, bb, cc, dd, ee, 6.0F, 7.0F, 8.0F);
	printf("result = %5.6f\n", result);
	return 0;
}
