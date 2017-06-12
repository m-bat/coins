/* hircIf.c
	HIR-C to HIR-base conversion
*/
/*
	Convert a conditional expression (forward)
	- Expand before if-statement.
*/
void if0()
{
	int a;

	if( --a )
	{
	}
	/*
	a = a - 1;
	if( a )
	{
	}
	*/
}

/*
	Convert a conditional expression (backward)
	 - Move conditional expression before if-statement
           by using temporal variable.
*/
void if1()
{
	int a;

	if(a--)
	{
	}
	/*
	int tmp;

	tmp = a;
	a = a - 1;
	if( tmp )
	{
	}
	*/
}
int main() /* SF030509 */
{
  printf("HIR conversion test of if\n"); /* SF030509 */
}
