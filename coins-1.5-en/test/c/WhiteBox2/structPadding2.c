/* structPadding2.c  Kitamura mail 030827 */

struct st1_t {
    char c0;
    int i0;
    short w;
    int i1;
    char c1;
};

struct st2_t {
    char c;
    struct st1_t st1;
    int j;
    char k;
};

struct st3_t {
    char c;
    char ca[3];
    char cc;
    struct st2_t st2;
    char ccc;
};

#if 1
struct st1_t st1 = {
    11, 12, 13, 14, 15
};

struct st2_t st2 = {
    100, { 101, 102, 103, 104, 105}, 106, 107
};
#endif

#if 1
struct st3_t st3 = {
    10, {11, }, 12,
    100, { 101, 102, 103, 104, 105}, 106, 107,
    13
};

int main()
{
    printf("%d %d %d %d %d\n", 
	   st1.c0,
	   st1.i0,
	   st1.w,
	   st1.i1,
	   st1.c1);
    printf("%d %d %d %d %d %d %d %d\n", 
	   st2.c, 
	   st2.st1.c0,
	   st2.st1.i0,
	   st2.st1.w,
	   st2.st1.i1,
	   st2.st1.c1,
	   st2.j,
	   st2.k);
    printf("%d %d %d %d %d %d %d %d %d %d %d %d\n", 
	   st3.c,
	   st3.ca[0],
	   st3.cc,
	   st3.st2.c, 
	   st3.st2.st1.c0,
	   st3.st2.st1.i0,
	   st3.st2.st1.w,
	   st3.st2.st1.i1,
	   st3.st2.st1.c1,
	   st3.st2.j,
	   st3.st2.k,
	   st3.ccc);
    return 0;
}
#endif

