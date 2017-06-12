/* tpmemaccess1.c:  Memory access test 1 */

int printf(char*, ...);

int gsi1;
float gsf1;
int gai1[4] = {1, 2, 3, 4};
char gsc1 = 'a';
char gac1[] = {'0', '1', '2', '3'};
struct point { float x; float y;}
  gstruct1[4];

int fn(int ps1, int pa[])
{
  int lsi1, i;
  int lai1[4] = {3, 2, 1, 0};
  lsi1 = pa[3] + lai1[3] + gai1[3] + (gac1[3] - '0');
  for (i = 2; i >= 0; i = i - 1) {
    lsi1 = lsi1 + pa[i] + lai1[i] + gai1[i] + (gac1[i] - '0');
  }
  return lsi1 * ps1;
}
int main()
{
  int lsi2, lsi3, i, j, k;
  struct point lstruct1;
  float laf2[4];
  j = 2;
  k = 1;
  laf2[0] = 3.0;
  for (i = 1; i < 4; i++) {
    laf2[i] =  laf2[i-1] * 2.0;
  }
  gstruct1[k].x = laf2[j];
  gstruct1[k].y = laf2[j+1];
  lstruct1.x = gstruct1[k].x + 4.0; 
  lstruct1.y = gstruct1[k].y + 5.0; 
  lsi3 = fn(j, gai1) + 6; 
  printf("%d, %f, %f\n", lsi3, lstruct1.x, lstruct1.y);
  return 0;
}
