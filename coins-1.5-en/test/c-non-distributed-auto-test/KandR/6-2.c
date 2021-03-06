struct point
{
	int x;
	int y;
};

struct rect
{
	struct point pt1;
	struct point pt2;
};

struct point makepoint(int x, int y)
{
	struct point temp;

	temp.x = x;
	temp.y = y;
	return temp;
};

#define XMAX 320
#define YMAX 200

f1()
{
	struct rect screen;
	struct point middle;
	struct point makepoint(int, int);

	screen.pt1 = makepoint(0, 0);
	screen.pt2 = makepoint(XMAX, YMAX);
	middle = makepoint((screen.pt1.x + screen.pt2.x)/2,
	                   (screen.pt1.y + screen.pt2.y)/2);
	printf("%d,%d\n", middle.x, middle.y);
}

struct point addpoint(struct point p1, struct point p2)
{
	p1.x += p2.x;
	p1.y += p2.y;
	return p1;
};

int ptinrect(struct point p, struct rect r)
{
	return p.x >= r.pt1.x && p.x < r.pt2.x
	    && p.y >= r.pt1.y && p.y < r.pt2.y;
};

#define min(a, b) ((a) < (b) ? (a) : (b))
#define max(a, b) ((a) > (b) ? (a) : (b))

struct rect canonrect(struct rect r)
{
	struct rect temp;
	temp.pt1.x = min(r.pt1.x, r.pt2.x);
	temp.pt1.y = min(r.pt1.y, r.pt2.y);
	temp.pt2.x = max(r.pt1.x, r.pt2.x);
	temp.pt2.y = max(r.pt1.y, r.pt2.y);
	return temp;
};

f2()
{
	struct point origin = { XMAX, YMAX }, *pp;

	pp = &origin;
	printf("origin is (%d,%d)\n", (*pp).x, (*pp).y);
	printf("origin is (%d,%d)\n", pp->x, pp->y);
}

f3()
{
	struct rect r = { 0, 0, XMAX, YMAX }, *rp = &r;  /* undeclared variable r. */
        /* In the book, struct rect r, *rp = r; */

	printf("%d %d %d %d\n",
		r.pt1.x,
		rp->pt1.x,
		(r.pt1).x,
		(rp->pt1).x);
}

f4()
{
	struct {
		int len;
		char *str;
	} tmp = { 0, "" }, *p = &tmp;
	
	++p->len;
	printf("%d\n", p->len);
}

main()
{
	f1();
	f2();
	f3();
	f4();
}
