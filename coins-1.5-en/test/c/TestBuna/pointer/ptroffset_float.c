int printf(char*, ...); /* int printf(char*, float); SF030514 */

int main(int argc, char **argv) {
  float data[6];
  float *p;
  float answer;
  data[0] = 1.1;
  data[1] = 2.2;
  data[2] = 3.3;
  data[3] = 4.4;
  data[4] = 5.5;
  data[5] = 0.0;
  p = data;
  answer = 0.0;

  answer += *p;
  answer += *(p+1);
  answer += *(p+2);
  answer += *(p+3);
  answer += *(p+4);
  printf("answer=%f\n", answer);
}
