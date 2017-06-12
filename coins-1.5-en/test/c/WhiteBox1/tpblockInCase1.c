/* tpblockInCase1.c Test of switch */
/*   Mori mail 021118 (no jump to default) */

int foo(int a)
{
    switch (a)
    {
    case 1:
	a = 10;
	break;
	{
	case 2:
	    a = 20;
	    break;
	case 3:
	    a = 30;
	    break;
	default:
	    a = 0;
	    break;
	}
    }
    return a;
}
 int main()
{
  int x;
  x = foo(5);
  printf("%d \n", x);
  return 0;
}

