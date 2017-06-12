int printf(char *s, ...);

double df=2.7182818284590452353602874713527F;
long double ldf=2.7182818284590452353602874713527F,ldd=2.7182818284590452353602874713527;

void fd(float f) {
  printf("%17.16g\n",(double)f);
}

void fld(float f,double d) {
  printf("%20.19Lg %20.19Lg\n",(long double)f,(long double)d);
}

int main() {
  float f;
  double d;

  printf("%17.16g\n",df);
  f=2.7182818284590452353602874713527F;
  printf("%17.16g\n",(double)f);
  printf("%17.16g\n",(double)f);
  fd(2.7182818284590452353602874713527F);

  printf("%20.19Lg %20.19Lg\n",ldf,ldd);
  f=2.7182818284590452353602874713527F; d=2.7182818284590452353602874713527;
  printf("%20.19Lg %20.19Lg\n",(long double)f,(long double)d);
  printf("%20.19Lg %20.19Lg\n",(long double)f,(long double)d);
  fld(2.7182818284590452353602874713527F,2.7182818284590452353602874713527);

  return 0;
}
