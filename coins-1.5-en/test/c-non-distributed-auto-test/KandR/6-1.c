#include <stdio.h>
#include <math.h>

struct point
{
	int x;
	int y;
};

struct
{
	int x;
	int y;
} x, y, z;

struct point pt;
struct point maxpt = { 320, 200 };

struct rect {
	struct point pt1;
	struct point pt2;
};

struct rect screen;

int main()
{
        double dist, sqrt(double);
	
	pt = maxpt;
	printf("%d,%d", pt.x, pt.y);
	
	dist = sqrt((double)pt.x * pt.x + (double)pt.y * pt.y);
	printf(" %f\n", dist);
	
	/* screen.pt1.x; */
	screen.pt1.x = 3; /* ##15 */
	
        return 0;
}
