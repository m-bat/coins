/* Sizeof/tpsizeof6.c  from bzip2.c */

/* from Spec2000/bzip2/bzip2.c */

typedef union _h_val {
  	unsigned long _i[sizeof(double) / sizeof(unsigned long)];
	double _d;
} _h_val;

extern const _h_val __huge_val;

/* from Spec2000/gap/eval.c */

typedef struct TypHeader {
    unsigned long       size;
    struct TypHeader    * * ptr;
    char                name[3];
    unsigned char       type;
}       * TypHandle;

struct TypHeader typH;

TypHandle       NewBag  ( unsigned int type,  unsigned long size ) 
{
  TypHandle th = &typH;
  th->type = type;
  th->size = size;
  th->name[0] = 'a';
  th->name[1] = 'b';
  th->name[2] = 'c';
  return th;  /*##*/
}

#define T_FUNCINT 16

main()
{
  int size1;
  _h_val hv;
  TypHandle           hdDef; 
    hdDef = NewBag( T_FUNCINT, sizeof(TypHandle(**)()) );
  size1 = sizeof(TypHandle(**)());
  printf("%d %d %d %d %d\n", size1, hdDef->type, hdDef->size, 
         sizeof(hv), sizeof(_h_val));
  return 0;
}

