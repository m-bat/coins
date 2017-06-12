/* tpqsort.c:  Quick sort  */

void Qsort(int v[30], int i, int j);
void Swap(int v[30], int i, int j);
int  Compare(int i, int j);
int  random();
int  printf(char *, ...);

int main()
{
  int Data[30];
  int i, j;

  for (i = 0; i < 30; i++)
    Data[i] = random() % 1000;

  Qsort(Data, 0, 29);

  for (i = 0; i < 30; i++)
    printf("%d ", Data[i]);
}

void Qsort(int v[30], int left, int right)
{
  int i, last;
  
  if (left >= right)
    return;

  Swap(v, left, (left + right) / 2);

  last = left;
  for (i = left + 1; i <= right; i++) {
    if (Compare(v[i], v[left]) > 0) {
      last++;
      Swap(v, last, i);
    }
  }
  Swap(v, left, last);
  Qsort(v, left, last - 1);
  Qsort(v, last + 1, right);
}

void Swap(int v[30], int i, int j)
{
  int  tmp;

  tmp = v[i];
  v[i] = v[j];
  v[j] = tmp;
}

int Compare(int i, int j)
{
  return (j - i);
}





