int printf(char*, ...); /* int printf(char*, unsigned int); SF030514 */

int main(int argc, char **argv) {
  unsigned int data[5];
  unsigned int *p;
  unsigned int answer;
  data[0] = 1;
  data[1] = 2;
  data[2] = 3;
  data[3] = 4;
  data[4] = 5;
  p = data;
  answer = 0;

  answer += *p;
  answer += *(p+1);
  answer += *(p+2);
  answer += *(p+3);
  answer += *(p+4);
  printf("answer=%d\n", answer);
}
