/* Call strcpy */

/*void *malloc(size_t size); */
void *malloc(long size); /* 051016 */
char *strcpy(char *s1, char *s2);
int strlen(char *s);
int puts(char *s);

#define LENGTH 50

int main(int argc, char **argv) {
	char *a, *str;
	a = "Hello World.";
	str = (char*)malloc(strlen(a) + 1);
	strcpy(str, a);
	puts(str);
	return 0;
}
