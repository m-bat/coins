int printf(char *s, ...);

int main() {
	int i;
	for ( i = 1; i < 10; i = i + 1){
	   if ( i == 4) continue;
	   if ( i >= 5) break;
	}
	printf("i = %d\n", i);
	return 0;
}