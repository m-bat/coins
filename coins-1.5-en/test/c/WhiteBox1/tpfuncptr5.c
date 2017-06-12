/* tpfuncptr5.c Function pointer in struct */
/*    in <reent.h> */

struct _atexit {
	struct	_atexit *_next;			 
	int	_ind;				 
	void	(*_fns[32 ])(void);	 
};

void f1() {
  printf("f1 is called\n");
};

void f2() {
  printf("f2 is called\n");
};

int main()
{
  struct _atexit exitTable;
  exitTable._ind = 0;
  exitTable._fns[0] = f1;
  exitTable._fns[1] = f2;
  exitTable._fns[0]();
  exitTable._fns[1]();
  return 0;
}


