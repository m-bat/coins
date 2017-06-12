int printf(char *s, ...);

int a[1]={ 12345 };

int main() {
  printf("%d\n",(*&a)[0]);
  return 0;
}
