/* tpAccessPath1.c:  Memory access path test 1 */

struct goods {
  char *name;
  int  unitPrice;
  int  count;
};

struct goods gNote;

int printf(char*, ...);
struct goods func(int pa[3], int pn, int *pp, struct goods pGoods);

int g1, g2;
int ga1[3] = {1, 2, 3};

int main()
{
  int a = 1, b = 2, c, d;
  int i = 0, j;
  int *ptrc, *ptrya;
  int sum, lPrice1, lPrice2;
  int xa[3];
  int ya[3] = {10, 20, 30};
  struct goods lAny;
  struct goods lPencil = {"Yotsubishi", 100, 10 };
  gNote.name      = "Nice";
  gNote.unitPrice = 150;
  gNote.count     = 5;
  ptrc  = &c;
  ptrya = ya;
  xa[i] = a;
  *ptrc = xa[i] + 1;
  lAny = func(ga1, 3, ptrya, lPencil);
  lPrice1 = gNote.unitPrice * gNote.count;
  lPrice2 = lPencil.unitPrice * lPencil.count;
  sum = c + lPrice1 + lPrice2; 
  *ptrya = lPrice1;
  ptrya++;
  *ptrya = lPrice2;
  *(ptrya+1) = sum;
  printf(" sum=%d ya = %d %d %d", sum, ya[0], ya[1], ya[2]);
  return 0; 
} 

struct goods func(int pa[3], int pn, int *pp, struct goods pGoods)
{
  int i;
  struct goods lResult;
  lResult = pGoods;
  for (i = 0; i < pn; i++) {
    lResult.count = lResult.count + pa[i];
  }
  return lResult;
}

