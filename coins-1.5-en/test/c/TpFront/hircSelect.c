/* hircSelect.c

	HIR-C to HIR-base conversion
*/

/*
        Ternary expression (? : expression)
	- ternary expression is represented as if-statement 
          by using temporal variable.
*/
int select0()
{
	int c, a, b;

	1 + c?a:b;
	/*
	int tmp;
	if(c) tmp=a; else tmp=b;
	1 + tmp;
	*/
}

/*
Ternary expression (expression statement)
*/
int select1()
{
	int c, a, b, r, *p;

	r = c?a:b; /* if(c) r=a; else r=b; */
	*p = c?a:b; /* if(c) *p=a; else *p=b; */
}

/*
        Ternary expression assignment to complicated l-value.
        - (Temporal variable is used.)
*/
int select2()
{
	int c, a, b, v[1];

	v[0] = c?a:b;
	/*
	int tmp;
	if(c) tmp=a; else tmp=b;
	v[0]=tmp;
	*/
}
int main() /* SF030509 */
{
  printf("HIR conversion test of ?:\n"); /* SF030509 */
}
