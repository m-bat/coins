/* jisTag1.c : JIS C 6.5.2.3 Tag p.64 */

#include <stdio.h>

  struct tnode {
    int count;
    struct tnode *left, *right;
  };
  struct tnode sx, sy, sz, *sp;
  typedef struct tnode1 TNODE1;
  struct tnode1 {
    int count;
    TNODE1 *left, *right;
  };
  struct s1 { struct s2 *s2pa; int ida; };
  struct s2 { struct s1 *s1pa; int counta; };
  struct s1 s1a;
  struct s2 s2a;

int 
main()
{
  struct s2;
  struct s1 { struct s2 *s2pb; int idb; };
  struct s2 { struct s1 *s1pb; int countb; };
  
  struct s1 s1b;
  struct s2 s2b;

  sx.count = 1;
  sx.left  = &sy;
  sx.right = &sz;
  sy.count = 2;
  sz.count = 3;
  sp = &sx;
  printf("sx.count %d sp->left->count %d \n",
          sx.count, sp->left->count);
  s1a.ida = 4;
  s2a.counta = 5;
  s1a.s2pa = &s2a;
  s2a.s1pa = &s1a;
  s1b.idb  = 6;
  s2b.countb = 7;
  s1b.s2pb   = &s2b;
  s2b.s1pb   = &s1b;
  printf("s1a.s2pa->counta %d s2a.s1pa->ida %d\n",
        s1a.s2pa->counta, s2a.s1pa->ida);
  printf("s1b.s2pb->countb %d s2b.s1pb->idb %d\n",
        s1b.s2pb->countb, s2b.s1pb->idb);
  return 0;
}

