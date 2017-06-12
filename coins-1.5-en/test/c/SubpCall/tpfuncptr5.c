/* tpfuncptr5.c Function pointer in struct */
/*    in <reent.h> */

struct _atexit {
	struct	_atexit *_next;
	int	_ind;
	void	(*_fns[32 ])(void);
};

void f() {
 printf("f() is called\n");
};

int main()
{
  struct _atexit exitTable;
  exitTable._ind = 0;
  exitTable._fns[0] = f;
  exitTable._fns[0]();
  printf("Function pointer in struct\n"); /* SF030609 */
  return 0;
}
