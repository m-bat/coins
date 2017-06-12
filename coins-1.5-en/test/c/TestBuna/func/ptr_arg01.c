/* Can pointer be an argument? */

int printf(char *s, ...);

float* get_addr(float *arg) {
	float *p;
	p = arg;
	return p;
}

int main() {
	float a, *ap;
	a = 83.01;
	ap = get_addr(&a);
	printf("*ap = %6.8f\n", *ap);
	return 0;
}