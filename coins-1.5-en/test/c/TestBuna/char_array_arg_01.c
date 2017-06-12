void print(char ch[12]) {
	int i;
	for (i = 0; i < 12; i++) {
		printf("ch[%d]=%c ", i, ch[i]);
	}
	printf("\n");
}

int main() {
	char array[] = { "hello world" };
	print(array);
}