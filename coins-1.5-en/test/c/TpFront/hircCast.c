/* hircCast.c
  Explicit conversion
    Types connected by --- can be casted

	float  --- int   --- pointer --- other pointer
	double     long
	           short
	           char
*/

void plainly_cast()
{
	int   i, *ip;
	float f, *fp;

	f = (float)i; /* int --- float */
	i = (int)f;

	i = (int)ip; /* int --- pointer */
	ip = (int*)i;

	ip = (int*)fp; /* pointer --- other pointer */
	fp = (float*)ip;
}

/*
  Implicit conversion
    Types connected by --- can be casted
    Conversion from int to pointer is permitted only if its value is 0

	float  --- int   --(warning)-- pointer --(warning)-- other pointer
	double     long
	           short
	           char
*/
void implicitly_cast()
{
	int   i, *ip;
	float f, *fp;

	f = i; /* int --- float */
	i = f;

	i = ip; /* int --(warinig)-- pointer */
	ip = i;

	ip = 0; /* 0 --- pointer */

	ip = fp; /* pointer --(warning)-- other pointer */
	fp = ip;
}
int main() /* SF030509 */
{
  printf("HIR conversion test of cast\n"); /* SF030509 */
}
