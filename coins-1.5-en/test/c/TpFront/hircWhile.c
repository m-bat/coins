/* hircWhile.c

	HIR-C to HIR-base conversion
*/

/*
        Expand condition expression of loop statement (forward expansion)
        - Move the conditional expression into loop body.
*/
int while0()
{
	int a;

	while(--a)
		1;
	/*
	while(true)
	{
		a=a-1;
		if(a)
			;
		else
			break;
		1;
	}
	*/
}

/*
        Expand condition expression of loop statement (backward expansion)
        - Move the conditional expression into loop body.
*/
int while1()
{
	int a;

	while(a--)
		1;
	/*
	while(true)
	{
		if(a)
			;
		else
			break;
		a=a-1;
		1;
	}
	*/
}
int main() /* SF030509 */
{
  printf("HIR conversion test of while\n"); /* SF030509 */
}
