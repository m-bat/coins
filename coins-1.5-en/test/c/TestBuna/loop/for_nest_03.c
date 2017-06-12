/* for_nest_03  nested for statement */
/* containing 2 for statement  */
int printf(char *s, ...);

int main() {
   short i, j, k;
   short a[3], b[3];
 
  for (i = 0; i < 3; i++) {
    a[i] = 0;
    b[i] = 0;
  }

  for (i = 0; i < 3; i++) {

    for (j = 0; j < 4; j++) {
      a[i] = a[i] + j;         
    }
    for (k = 0; k < 5; k++) {
      b[i] = b[i] + k;
    }
  }
  for (i = 0; i < 3; i++) {
    printf(" i   = %d\n", i );
    printf("a[i] = %d\n", a[i]);
    printf("b[i] = %d\n", b[i]);
  } 
  return 0; 
}
