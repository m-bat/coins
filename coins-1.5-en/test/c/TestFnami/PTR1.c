int printf(char *s, ...);

int a[1]={ 12345 };

int main() {
  int *i=(&a)[0];
  printf("%d\n",*i);
  return 0;
}
