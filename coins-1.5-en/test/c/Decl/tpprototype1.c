/* tpprototype1.c  Prototype declaration (Decl) */

double point2fault(double x[]);

double point2fault(x)
double x[];
{
  return 0.0;
} 

int main()
{
  double aa[10], d;
  d = point2fault(aa);
  printf("d=%f \n",d); /* SF030620 */
  return 0;
}

