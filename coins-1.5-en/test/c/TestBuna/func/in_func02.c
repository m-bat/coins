/*
 Can call function in a function?
 (more difficult one)
*/

int printf(char *s, ...);

int get_len(char *s) {
	int i;
	char ch;
	ch = s[0];
	for (i = 0; ch != '\0'; i++) {
		ch = s[i];
	}
	return i;
}

int main() {
	int a, b, c;
	char *str;
	str = "abcde";
	a = 0;
	if (get_len(str) == 0) {
		printf("error");
	} else {
		printf(str);
		printf("\n");
	}
	return 0;
}