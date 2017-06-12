/* Return value test(double) */

int printf(char *s, ...);

double func() {
	double ret;
	ret = 99.843;
	return ret;
}

int main() {
	double a;
	a = func();
	printf("a = %5.10f\n", a);
	return 0;
}