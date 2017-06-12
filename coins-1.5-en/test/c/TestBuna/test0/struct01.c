int printf(char *s, ...);

struct datas {
	int num1;
	int num2;
};

int main() {
	struct datas d;
	d.num1 = 688;
	d.num2 = 55;
	printf("d.num1 = %d, d.num2 = %d\n", d.num1, d.num2);
	return 0;
}