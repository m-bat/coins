/* tpcast1.c
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

	f = (float)i; 
	i = (int)f;

	i = (int)ip; 
	ip = (int*)i;

	ip = (int*)fp; 
	fp = (float*)ip;

}

/*
  Implicit conversion
    Types connected by --- can be casted
    Conversion from int to pointer is permitted only if the value is 0

	float  --- int   --(warning)-- pointer --(warning)-- other pointer
	double     long
	           short
	           char
*/
void implicitly_cast()
{
	int   i, *ip;
	float f, *fp;

	f = i; 
	i = f;

	i = ip; 
	ip = i;

	ip = 0; 

	ip = fp; 
	fp = ip;
}

/* SF030620[ */
int main()
{
  printf("'assignment makes pointer from integer without a cast' test\n");
  printf("'incompatible pointer type' test\n");
  return 0;
}
/* SF030620] */


