/* Argument spill test(char) */
int printf(char *s, ...);

char sub(
	char a, char b, char c, char d, char e, char f,
	char g, char h
) {
	char res;
	res = a - b - c - d - e - f - g - h;
	return res;
}

int main() {
	char aa, bb, cc, dd, ee, sum;
	aa = 1;
	bb = 2;
	cc = 3;
	dd = 4;
	ee = 5;
	sum = sub(aa, bb, cc, dd, ee, 21, 99, 5);
	printf("sum = %d\n",sum); /* SF030514 */
	return 0;
}
