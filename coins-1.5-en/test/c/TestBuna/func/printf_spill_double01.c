/* Check argument spill by double */

int printf(char *s, ...);

int main() {
	double a, b, c, d;
	a = 1.1;
	b = 2.22222222222;
	c = 2.33333333333;
	d = 2.412344993;
	printf(
		"a = %5.8f, b = %5.7f, c = %5.6f, d = %5.5f\n",
		a, b, c, d
	);
	return 0;
}