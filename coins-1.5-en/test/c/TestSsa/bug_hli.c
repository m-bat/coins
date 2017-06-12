/* bug_hli.c */
/* SSA HoistingLoopInvariant Bug and check of inline expansion */

#include <stdio.h>

void bug_hli(int x, int y);

main() {
  bug_hli(1, 0);
}

void bug_hli(int x, int y) {
  int i, t;

  for (i = 0; i < 10; i++) {
    if (y <= i && y == 0) {
      printf("x = %d  y = %d\n", x, y); return;
    }
    t = x / y;
  }

  printf("x / y = %d\n", t);

  return;
}

