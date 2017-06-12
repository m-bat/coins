int printf(char *s, ...);

int main() {
  printf("%d %d %d %d %d\n",(int)sizeof(  signed char),(int)sizeof(         short),
         (int)sizeof(         int),(int)sizeof(         long),(int)sizeof(         long long));
  printf("%d %d %d %d %d\n",(int)sizeof(unsigned char),(int)sizeof(unsigned short),
         (int)sizeof(unsigned int),(int)sizeof(unsigned long),(int)sizeof(unsigned long long));
  printf("%d %d %d %d\n",   (int)sizeof(         char),
         (int)sizeof(float),(int)sizeof(double),(int)sizeof(long double));
  return 0;
}
