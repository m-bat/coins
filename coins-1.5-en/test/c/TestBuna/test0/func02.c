int printf(char *s, ...);

float max(float a, float b);

int main() {
	float a, b, c;
	a = 32.0;
	b = max(a,max(10.0, 33.0));
	printf("b = %f\n", b);
	return 0;
}

float max(float a, float b) {
	float c;
	if (a > b) {
		c = a;
	} else {
		c = b;
	}
	return c;
}
