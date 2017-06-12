int printf(char *str, ...);
double max(double a, double b);

int main() {
	double a, b, c;
	a = 32.0;
	b = max(a,max(10.0, 33.0));
	printf("b = %f\n", b);
	return 0;
}

double max(double a, double b) {
	double c;
	if (a > b) {
		c = a;
	} else {
		c = b;
	}
	return c;
}

