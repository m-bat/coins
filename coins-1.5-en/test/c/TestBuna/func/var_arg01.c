/* Can float variable be an argument? */
int printf(char *s, ...);

float func(float arg) {
	float a;
	a = arg;
	return a;
}

int main() {
	float a, val;
	a = 67.15;
	val = 32.85 + func(a);
	printf("val = %5.5f\n", val);
	return 0;
}