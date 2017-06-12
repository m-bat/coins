#ifdef NDEBUG
#define assert(p)  ((void)0)
#else
#define assert(e) __assert(e, __FILE__, __LINE__)
#endif

#include <stdio.h>
void abort(void);

void __assert(int e, char *filename, int nnn){
  if (e) return;
  printf("Assertion failed: file %s, line %d\n", filename, nnn);
  abort();
}
