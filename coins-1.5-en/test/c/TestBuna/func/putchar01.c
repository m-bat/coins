#define EOF (-1)
int putchar(int a);
int getchar();

int main() {
	int ch;
	for (;;) {
		ch = getchar();
		if (ch == EOF) {
			break;
		}
		putchar(ch);
	}
	return 0;
}