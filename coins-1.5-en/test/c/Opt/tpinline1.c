/* tpinline1.c: inline specifier */
/*    ISO/IEC 9899:1999 6.7.4    */ 

extern int printf(char *, ...);

inline double fahr(double t)  /* Parse Error "no type specifier" */
{
  return (9.0*t)/5.0 + 32.0;
}

inline double cels(double t)
{
  return (5.0 * (t - 32.0)) / 9.0;
}

extern double fahr(double); 

double convert(int is_fahr, double temp)
{
  return is_fahr ? cels(temp) : fahr(temp);
}

int main()
{
  double t1, t2;
  t1 = convert(0, 10.0);
  t2 = convert(1, 32.0);
  printf("t1=%f, t2=%f \n", t1, t2); /* SF030620 */
}

