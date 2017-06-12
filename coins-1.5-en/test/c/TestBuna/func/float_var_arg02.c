/* Can float variable be an argument? */
int printf(char *s, ...);

float func(float arg1, float arg2) {
	float a;
	a = arg1 * arg2;
	return a;
}

int main() {
	float a, b, val;
	a = 67.15;
	b = -39.32;
	val = 32.85 + func(a, b);
	printf("val = %5.5f\n", val);
	return 0;
}