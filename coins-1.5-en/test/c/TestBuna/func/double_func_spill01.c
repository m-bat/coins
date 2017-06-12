/* Argument spill test(double) */
int printf(char *s, ...);

double sub(
	double a, double b, double c, double d, double e, double f,
	double g, double h
) {
	double res;
	res = a - b - c - d - e - f - g - h;
	return res;
}

int main() {
	double aa, bb, cc, dd, ee, result;
	aa = 1.0;
	bb = 2.0;
	cc = 3.0;
	dd = 4.0;
	ee = 5.0;
	result = sub(aa, bb, cc, dd, ee, 6.0, 7.0, 8.0);
	printf("result = %5.5f\n", result);
	return 0;
}
