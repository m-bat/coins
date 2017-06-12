/* converr.c (TestBuna) */

int printf(char *str, ...);

int main(){
  int si = 100;
  float f = 1.1;
  f = f * 3 / si;  /* 3 and si are converted to double */

  printf("%f\n", f);
}
