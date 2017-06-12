/* hirBase1.c

	HIR-C to HIR-base conversion
*/

int getchar();
int putchar(int i);

int main()
{
	int c;
	while ((c = getchar())>=0 )
		putchar(c);
}
/*
int main()
{
	while(true)
	{
		int c;
		c = getchar();
		if( c>=0 )
			;
		else
			break;
		putchar(c);
	}
}
*/
