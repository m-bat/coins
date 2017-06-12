/* tpprototype2.c  Prototype declaration (Decl) */

struct properties {

  double cp;        /* compressional wave velocity */
  double cs;        /* shear wave velocity */
  double den;       /* density */

};

struct properties prop;

/*
double point2fault(double x[]);
*/
void element_matrices(double vertices[][3], 
           struct properties *prop, double Ke[][12], double Me[]); 

double point2fault(x)
double x[];
{
  return 0.0;
} 

void element_matrices(double vertices[][3], 
           struct properties *prop, double Ke[][12], double Me[])
{ /* ... */ 
} 

int main()
{
  double aa[10], d;
  double bb[4][3], kk[10][12], mm[10];
  d = point2fault(aa);
  element_matrices(bb, &prop, kk, mm); 
  /* SF030620[ */
  printf("d=%f \nelement_matrices won't do anything(only prototype)\n", d);
  /* SF030620] */
  return 0;
}

