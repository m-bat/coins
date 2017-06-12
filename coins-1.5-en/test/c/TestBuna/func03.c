
int func(int a, int b) {
  int c = 3;
  int d = 4;

  return a+b+c+d;
}
int main(){
  int a = 1;
  int b = 1;
  printf("%d\n",func(a,b)); /* SF030514 */
  return func(a,b);
}

int func2(){
  int e;
  int f;
}
