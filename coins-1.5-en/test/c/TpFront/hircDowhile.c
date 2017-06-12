/* hircDowhile.c
	HIR-C to HIR-base conversion
*/

/*
	do-while expansion (forward expansion)
        - Expand conditon expression in the loop body.
*/
int dowhile0()
{
	int a;

	do
		1;
	while(--a) ;
	/*
	do
	{
		1;
		a=a-1;
		if(a)
			;
		else
			break;
	}
	while(true);
	*/
}

/*
	do-while conversion (backward expansion)
        - Expand conditon expression in the loop body.
*/
int dowhile1()
{
	int a;

	do
		1;
	while(a--);
	/*
	do
	{
		1;
		if(a)
			;
		else
			break;
		a=a-1;
	}
	while(true);
	*/
}
int main() /* SF030509 */
{
  printf("HIR conversion test of do-while\n"); /* SF030509 */
}
