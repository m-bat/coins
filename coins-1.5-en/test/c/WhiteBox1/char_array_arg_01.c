/* char_array_arg01.c character array argument */

int printf(char*, ...);

void print(char ch[12]) {
	int i;
	for (i = 0; i < 12; i++) {
		printf("ch[%d]=%c ", i, ch[i]);
	}
	printf("\n");
}

int main() {
	char array1[] =  "hello world" ;
	print(array1);
	char array[] = { "hello world" }; /* This should be char array[] = "hello world"; */
	print(array);
}
