/* tpstructcompati.c:  2 dim C array and Java style array access. */
/*   Hasegawa mail 030908 java Alias -coins:alias=opt -S xxx.c */

int printf(char*, ...);

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
  xb = &xp;
  xp.ptr[2] = &xp1[0];
  xp1[0].objPtr = &x[0];
  x[0].elem[0] = 10;
  x[0].elem[1] = 20;
  x[0].elem[2] = 30;
  printf("xb->ptr[2]->objPtr->elem[%d %d %d]\n",
	 xb->ptr[2]->objPtr->elem[0],
	 xb->ptr[2]->objPtr->elem[1],
	 xb->ptr[2]->objPtr->elem[2]);
} 
