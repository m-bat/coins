/* KandR/6-9.c Bit field test */

struct {
	unsigned int is_keyword : 1;
	unsigned int is_extern  : 1;
	unsigned int is_static  : 1;
} flags;

main()
{
	flags.is_extern = flags.is_static = 1;
	flags.is_keyword = 0;
	if( flags.is_extern == 0 && flags.is_static == 0 )
		;
  printf("%d %d \n", flags.is_extern, flags.is_static);
}
