#define NULL  ((void*)0)


typedef long Align;

union header {
	struct {
		union header *ptr;
		unsigned size;
	} s;
	Align x;
};

typedef union header Header;

static Header base;
static Header *freep = NULL;

int mem[10000];

void *mallocx (unsigned nbytes) /* malloc may be treated as library function. */
{
	Header *p, *prevp;
	Header *morecore(unsigned);
	unsigned nunits;

	nunits = (nbytes+sizeof(Header)-1)/sizeof(Header) + 1;
	if ((prevp = freep) == NULL) {
		base.s.ptr = freep = prevp = &base;
		base.s.size = 0;
	}
	for (p = prevp->s.ptr; ; prevp = p, p = p->s.ptr) {
		if (p->s.size >= nunits) {
			if (p->s.size == nunits)
				prevp->s.ptr = p->s.ptr;
			else {
				p->s.size -= nunits;
				p += p->s.size;
				p->s.size = nunits;
			}
			freep = prevp;
			return (void *)(p+1);
		}
		if (p == freep)
			if ((p = morecore(nunits)) == NULL)
				return NULL;
	}
}

/* #define NALLOC 1024 */
#define NALLOC 512

static Header *morecore(unsigned nu)
{
	char *cp, *sbrkx(int);
	Header *up;
	void freex(void *);

	if (nu < NALLOC)
		nu = NALLOC;
	cp = sbrkx(nu * sizeof(Header)); 
	if (cp == (char *) -1)
		return NULL;
	up = (Header *) cp;
	up->s.size = nu;
	freex((void *)(up+1));
	return freep;
}

char *sbrkptr = (char*)&mem[0];

char *sbrkx(int pSize)  /* sbrk may be treated as library function. */
{
  char *lptr = sbrkptr;
  sbrkptr = sbrkptr + pSize;
  if (sbrkptr > (char*)&mem[9999])
    lptr = (char*)-1;
  return lptr;
}

void freex(void *ap) /* free may be treated as library function */
{
	Header *bp, *p;

	bp = (Header *)ap - 1;
	for (p = freep; !(bp > p && bp < p->s.ptr); p = p->s.ptr)
		if (p >= p->s.ptr && (bp > p || bp < p->s.ptr))
			break;

	if (bp + bp->s.size == p->s.ptr) {
		bp->s.size += p->s.ptr->s.size;
		bp->s.ptr = p->s.ptr->s.ptr;
	} else
		bp->s.ptr = p->s.ptr;
	if (p + p->s.size == bp) {
		p->s.size += bp->s.size;
		p->s.ptr = bp->s.ptr;
	} else
		p->s.ptr = bp;
	freep = p;
}

main()
{
  /* SF030620[ */
  int i, j;
  int *p[100];

  /* for( i=0; i<100; i++ ) */ 
  for( i=0; i<80; i++ ) /* Consider the space overhead caused by Header */ 
  {
    p[i] = mallocx(sizeof(int)*100); /* malloc(sizeof(int)*100) */
    for( j=0; j<100; j++ )
      p[i][j] = 100*i+j;
  }
  /* for( i=0; i<100; i++ ) */
  for( i=0; i<80; i++ ) /* Consider the space overhead caused by Header */ 
  {
    printf("%d ",p[i][99]);
    freex(p[i]); /* free */
  }
  printf("\n");
  /* SF030620] */
}
