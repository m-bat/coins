int printf(char *s, ...);

char f0(void);
char f0(void);
char f1();
char f1(void);
char f2(void);
char f2();
short f3(int,double);
short f3(int,double);
short f4();
short f4(int,double);
short f5(int,double);
short f5();
int f6(float (*)[10],void (*)(    ));
int f6(float (*)[  ],void (*)(long));
int f7(float (*)[  ],void (*)(long));
int f7(float (*)[10],void (*)(    ));
int f8(float (*)[10],void (*)(    ));
int f8(float (*)[10],void (*)(long));
int f9(float (*)[  ],void (*)(long));
int f9(float (*)[10],void (*)(long));
int g6();
int g6(float (*)[10],void (*)(    ));
int g6(float (*)[  ],void (*)(long));
int g7();
int g7(float (*)[  ],void (*)(long));
int g7(float (*)[10],void (*)(    ));
int g8();
int g8(float (*)[10],void (*)(    ));
int g8(float (*)[10],void (*)(long));
int g9();
int g9(float (*)[  ],void (*)(long));
int g9(float (*)[10],void (*)(long));

char f0() { return 0; }
char f1() { return 1; }
char f2() { return 2; }
short f3(int x,double y) { return 3; }
short f4(int x,double y) { return 4; }
short f5(int x,double y) { return 5; }
int f6(float (*ap)[10],void fp(long)) { return 6; }
int f7(float (*ap)[10],void fp(long)) { return 7; }
int f8(float (*ap)[10],void fp(long)) { return 8; }
int f9(float (*ap)[10],void fp(long)) { return 9; }
int g6(float (*ap)[10],void fp(long)) { return 6; }
int g7(float (*ap)[10],void fp(long)) { return 7; }
int g8(float (*ap)[10],void fp(long)) { return 8; }
int g9(float (*ap)[10],void fp(long)) { return 9; }

char f0(),f1(),f2();
short f3(),f4(),f5();
int f6(),f7(),f8(),f9();
int g6(),g7(),g8(),g9();

int main() {
  char (*fp0)(void)=f0;
  char (*fp1)(void)=f1;
  char (*fp2)(void)=f2;
  short (*fp3)(int,double)=f3;
  short (*fp4)(int,double)=f4;
  short (*fp5)(int,double)=f5;
  int (*fp6)(float (*)[10],void (*)(long))=f6;
  int (*fp7)(float (*)[10],void (*)(long))=f7;
  int (*fp8)(float (*)[10],void (*)(long))=f8;
  int (*fp9)(float (*)[10],void (*)(long))=f9;
  int (*gp6)(float (*)[10],void (*)(long))=g6;
  int (*gp7)(float (*)[10],void (*)(long))=g7;
  int (*gp8)(float (*)[10],void (*)(long))=g8;
  int (*gp9)(float (*)[10],void (*)(long))=g9;
  printf("%d %d %d\n",fp0(),fp1(),fp2());
  printf("%d %d %d\n",fp3(0,0),fp4(0,0),fp5(0,0));
  printf("%d %d %d %d\n",fp6(0,0),fp7(0,0),fp8(0,0),fp9(0,0));
  printf("%d %d %d %d\n",gp6(0,0),gp7(0,0),gp8(0,0),gp9(0,0));
  return 0;
}
