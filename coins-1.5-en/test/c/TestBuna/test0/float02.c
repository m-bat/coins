int printf(char *s, ...);

int main() {
	float a, b;
	a = 444.44;
	b = a + 32.3;
	
	printf("b = %5.5f\n", b);
	return 0;
}