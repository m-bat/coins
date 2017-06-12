int printf(char *s, ...);

typedef unsigned int u_int;
typedef unsigned long u_long;

int main() {
  float f=1.5F;
  u_long ul=3000000000UL;
  long l=-10;
  u_int ui=3000000001UL;
  int i=-11;

  printf("%d\n",(int)sizeof(1. + f));
  printf("%d\n",(int)sizeof(1. +ul));
  printf("%d\n",(int)sizeof(1. + l));
  printf("%d\n",(int)sizeof(1. +ui));
  printf("%d\n",(int)sizeof(1. + i));
  printf("%d\n",(int)sizeof(1.F+ul));
  printf("%d\n",(int)sizeof(1.F+ l));
  printf("%d\n",(int)sizeof(1.F+ui));
  printf("%d\n",(int)sizeof(1.F+ i));
  printf("%d %lu\n",(int)sizeof(1UL+ l),(1UL+ l)>>16);
  printf("%d %lu\n",(int)sizeof(1UL+ui),(1UL+ui)>>16);
  printf("%d %lu\n",(int)sizeof(1UL+ i),(1UL+ i)>>16);
  printf("%d %lu\n",(int)sizeof(1L +ui),(1L +ui)>>16);
  printf("%d %ld\n",(int)sizeof(1L + i),(1L + i)>>16);
  printf("%d %u\n" ,(int)sizeof(1U + i),(1U + i)>>16);

  printf("%d\n",(int)sizeof( f+1. ));
  printf("%d\n",(int)sizeof(ul+1. ));
  printf("%d\n",(int)sizeof( l+1. ));
  printf("%d\n",(int)sizeof(ui+1. ));
  printf("%d\n",(int)sizeof( i+1. ));
  printf("%d\n",(int)sizeof(ul+1.F));
  printf("%d\n",(int)sizeof( l+1.F));
  printf("%d\n",(int)sizeof(ui+1.F));
  printf("%d\n",(int)sizeof( i+1.F));
  printf("%d %lu\n",(int)sizeof( l+1UL),( l+1UL)>>16);
  printf("%d %lu\n",(int)sizeof(ui+1UL),(ui+1UL)>>16);
  printf("%d %lu\n",(int)sizeof( i+1UL),( i+1UL)>>16);
  printf("%d %lu\n",(int)sizeof(ui+1L ),(ui+1L )>>16);
  printf("%d %ld\n",(int)sizeof( i+1L ),( i+1L )>>16);
  printf("%d %u\n" ,(int)sizeof( i+1U ),( i+1U )>>16);

  return 0;
}
