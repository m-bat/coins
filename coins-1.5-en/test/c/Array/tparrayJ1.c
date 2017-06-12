/* tparrayJ1.c:  2 dim C array and Java style array access. */
int i, j, k;

struct intArrayBody {
  int  elem[3];
  int *classPtr;
}; 
struct objPtrRec {
  struct intArrayBody *objPtr;
  short  Length;
  short  Type;
};
struct intPtrArray {
  struct objPtrRec *ptr[5];
  int              *classPtr;
};
struct objPtr1 {
  struct intPtrArray *objPtr;
  short  Length;
  short  Type;
};

struct intPtrArray *xb;
struct intPtrArray  xp;
struct objPtrRec    xp1[5];
struct intArrayBody x[5];
int                 y[5][3];

int main()
{
  xp.ptr[0] = &xp1[0];
  xp.ptr[1] = &xp1[1];
  xp.ptr[2] = &xp1[2];
  xp.ptr[3] = &xp1[3];
  xp.ptr[4] = &xp1[4];
  xp1[0].objPtr = &x[0];
  xp1[1].objPtr = &x[1];
  xp1[2].objPtr = &x[2];
  xp1[3].objPtr = &x[3];
  xp1[4].objPtr = &x[4];
  /* SF030620[ */
  x[2].elem[0] = 20;
  x[2].elem[1] = 21;
  x[2].elem[2] = 22;
  /* SF030620] */
  i = 1;
  j = 4;
  /* SF030620[ */
  xb=&xp;
  printf("xb->ptr[2]->objPtr->elem[%d %d %d]\n",
	 xb->ptr[2]->objPtr->elem[0],
	 xb->ptr[2]->objPtr->elem[1],
	 xb->ptr[2]->objPtr->elem[2]);
  /* SF030620] */
  xb->ptr[3]->objPtr->elem[2] = 0; 
  xb->ptr[i]->objPtr->elem[j] = 5;
  y[i][j] = 5;
  k = xb->ptr[1]->objPtr->elem[3];
  k = k + y[1][3];
  printf("k=%d \n",k); /* SF030620 */
  return k;
} 
