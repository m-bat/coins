/* hirBase2.c

	HIR-C to HIR-base conversion
*/

int sample1()
{
	int i, j;
	if (i > 0 && i < 10) j = 1;
	/*
		if( i>0 )
			if( i<10 )
				j = 1;
	*/
}
int sample2()
{
	int i, j;

	if (i == 1 || j == 2) j = 3;

	/*
	if( i==1 )
		LABEL2: j = 3;
	else
		if( j==2 )
			goto LABEL2;
		else
			j = 3;
	*/
}
int sample3()
{
	int i, j, x;

	x = i == 0 && j == 0;

	/*
	if( i==0 )
		if( j==0 )
			x = true;
		else
			goto LABEL0;
	else
		LABEL0: x = false;
	*/
}
int sample4()
{
	int z, a, b;

	z = (a > b) ? a : b;
	/*
	if( a>b )
		z = a;
	else
		z = b;
	*/
}
int sample5()
{
	int z, a, x;
	int f(int);

	z = ((a < 0)||((x = f(a)) > 10)) ? f(0) : a;
	/*
	if( a<0 )
		LABEL5: z = f(0);
	else
	{
		x = f(a);
		if( x>10 )
			goto LABEL5;
		else
			z = a;
	}
	*/
}
int sample6()
{
	int z, a, i, b, j;

	z = ((--a == i)&&(b++ == j) ? i : j);
	/*
	a = a - 1;
	if( a==i )
	{
		tmp = b==j;
		b = b + 1;
		if( tmp )
			z = i;
		else
			goto LABEL6;
	}
	else
		LABEL6; z = j;
	*/
}
int f(int v) /* SF030509 */
{
  return v; /* SF030509 */
}
int main() /* SF030509 */
{
  printf("HIR conversion test\n"); /* SF030509 */
}
