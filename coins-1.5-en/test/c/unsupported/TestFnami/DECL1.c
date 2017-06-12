int printf(char *s, ...);

int p0=123;
int *p1=&p0;
int **p2=&p1;
int ***p3=&p2;
int ****p4=&p3;
int *****p5=&p4;
int ******p6=&p5;
int *******p7=&p6;
int ********p8=&p7;
int *********p9=&p8;
int **********pa=&p9;
int ***********pb=&pa;
int ************pc=&pb;

int main() {
  printf("%d\n",************pc);
  return 0;
}
