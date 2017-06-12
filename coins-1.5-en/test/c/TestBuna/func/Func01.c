int printf(char *s, ...); 
double max(double a, double b);

int main() {
	double a, b, c;
	a = 32.0;
	b = max(a, 31.0);
	printf("b = %f\n", b);
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
