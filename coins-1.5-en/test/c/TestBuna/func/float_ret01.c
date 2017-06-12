/* Return value test(float) */

int printf(char *s, ...);

float func() {
	float ret;
	ret = 1.1;
	return 99.843;
}

int main() {
	float a;
	a = func();
	printf("a = %5.10f\n", a);
	return 0;
}