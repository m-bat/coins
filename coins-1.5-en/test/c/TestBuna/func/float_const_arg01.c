/* Can float const be an argument? */

int printf(char *s, ...);

float func(float arg) {
	float a;
	a = arg;
	return a;
}

int main() {
	float val;
	val = func(111.23483);
	printf("val = %5.5f\n", val);
	return 0;
}