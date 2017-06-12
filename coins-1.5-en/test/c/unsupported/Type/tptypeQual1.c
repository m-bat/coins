/* tptypeQual1: Type qualifier */

const int c1 = 1;
const int *pi;
volatile int v1;

struct str0 {
  int kind;
  char *name;
};

struct str1 {
  const int kind;
  char *name;
};

int main()
{
  /**
  struct str1 {
    const int kind;
    char *name;
  };
  **/ /* two or more data types in declaration of main ? */
  const struct str0 stra[2] = {{1, "a"}, {2, "b"}};
  struct str0 st00, st01;
  struct str1 st10 = {1, "name1" };
  struct str0 {
    int kind;
    char *name;
  } st02;
  volatile struct str1 st11;
  st00 = st10;  /* incompatible types in assignment */
  st01 = stra[0];
  st02 = stra[1];
  st11.name = "x";

  printf("This test should cause 'incompatible types' error.\n"); /* SF030509 */

  return 0;
}

