/* tpstructFunc2.c  -- structure parameter */

struct point {
  double x;
  double y;
};

struct point p1;

double sqrt(double);

double line(struct point pa, struct point pb)
{
  double length;

  length = sqrt((pa.x - pb.x)*(pa.x - pb.x) + (pa.y - pb.y)*(pa.y - pb.y));
  return length;
}

int main()
{
  struct point p10, p20;
  double leng1;
  p10.x = 1.0;
  p10.y = 2.0;
  p20.x = 4.0;
  p20.y = 6.0;
  printf("p10 = [%f,%f]\n",p10.x,p10.y); /* SF030609 */
  printf("p20 = [%f,%f]\n",p20.x,p20.y); /* SF030609 */
  leng1 = line(p10, p20);
  printf("length = %f\n",leng1); /* SF030609 */
  return 0;
}

