/* for_nest_02  nested for statement */
                           /* array  */
int printf(char *s, ...);

int main() {
	int i,j;
        int a[2][3];
	for (i = 0; i < 2; i++) {
           for (j = 0; j < 3; j++) {
		a[i][j]= i*10+(j+1);
           }
        }
	printf("a[0][0] = %d\n", a[0][0]);
        printf("a[0][1] = %d\n", a[0][1]);
        printf("a[0][2] = %d\n", a[0][2]);
	printf("a[1][0] = %d\n", a[1][0]);
        printf("a[1][1] = %d\n", a[1][1]);
        printf("a[1][2] = %d\n", a[1][2]);
	return 0;
}