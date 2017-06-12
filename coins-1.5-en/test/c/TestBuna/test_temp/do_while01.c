int printf(char *s, ...);

int main(){
	int a;
	int b;
	int z;
	int temp;

	a=1;
	b=2;

	z=0;

	do {
	  temp=a;
	  a=b;
	  b=temp;
	  z=z+1;
	} while(z<3);
	a = a-1;
	b = b-1;
	temp = temp-1;
	z = z-1;
	printf("z = %d\n", z);
	return 0;
}