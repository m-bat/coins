/* tpcall8.c  -- Fujise mail 020927 */

int main()
{
  int a;
  a = 1;
  a + sub(1);
  a = a+sub(2);
}

int sub(int x)
{
    printf("value(%d)\n",x);
    return x;
}

