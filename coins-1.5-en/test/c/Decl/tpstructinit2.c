/* tpstructinit2.c  Initial value for structure */
/* (Decl) */

struct point { int x; int y; } p1 = {1, 2};
struct point p2[3] = { {2, 3}, {4, 5}, {6, 7} };
struct point pa[10];
struct student { char *name; int id; } s1 = { "Tanaka", 100 };
struct student s2 = { "Suzuki", 101 };
struct student g1[2] = { {"Kimura", 102}, {"Seki", 204} };
struct student g3[10];

int
main()
{
  struct point p3 = {11, 12}, p4;
  struct student s3 = {"Noda", 105}, s4;
  p4 = p1;
  g3[1] = s3; 
  printf("p4=(%d,%d) g3[1]={%s,%d}\n",p4.x,p4.y,g3[1].name,g3[1].id); /* SF030620 */
  return 0;
}

