int printf(char *s, ...);

int main() {
	int i;
	i = 1;
	switch ( i ) {
	case 1:
		i = 1;
		break;
	case 2:
		i = 2;
		break;
	default:
		i = 3;
		break;
	}
	printf("i = %d\n", i);
	return 0;
}