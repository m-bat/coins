int printf(char*, ...);

int main() 
{
  int i = 0;
  switch (i) {
  default: printf("default\n");
  case 1: printf("one\n");
  case 2: printf("two\n");
  }
}
