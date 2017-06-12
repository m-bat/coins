int main() {
	char str[10];
	str[0] = 'a';
	str[1] = 'b';
	str[2] = 'c';
	str[3] = 0; /* SF030514 */
	printf("str = %s\n",str); /* SF030514 */
	return 0;
}