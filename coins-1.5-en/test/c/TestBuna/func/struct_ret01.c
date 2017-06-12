/* Return value test(struct) */

int printf(char *s, ...);

struct data {
	int num1;
	double num2;
};

struct data func() {
	struct data ret;
	ret.num1 = 84458;
	ret.num2 = 333.33;
	return ret;
}

int main() {
	struct data d;
	d = func();
	printf("d.num2 = %5.5f\n", d.num2);
	return 0;
}