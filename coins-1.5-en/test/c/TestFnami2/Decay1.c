int printf(char *s, ...);

short a[3]={ 123,456,789 };

int main() {
  char (*cap)[5]=&"ABCD";
  short (*p)[3]=&a;
  printf("%d\n",(int)sizeof a);
  printf("%c %d\n",(*(&"ABCD"))[1],(*(&a))[1]);
  printf("%c %d\n",(*cap)[2],(*p)[2]);
  return 0;
}
